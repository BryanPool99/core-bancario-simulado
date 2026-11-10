package com.bryandev.transfer_service.service.impl;

import com.bryandev.transfer_service.mapper.TransferMapper;
import com.bryandev.transfer_service.model.dto.request.TransferRequestDto;
import com.bryandev.transfer_service.model.dto.response.TransferResponseDto;
import com.bryandev.transfer_service.repository.TransferRepository;
import com.bryandev.transfer_service.service.TransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferServiceImpl implements TransferService {

    private final TransferRepository repository;
    private final TransferMapper mapper;

    private final RestClient accountServiceRestClient;

    @Override
    @Transactional
    public TransferResponseDto executeTransfer(TransferRequestDto requestDto) {
        log.info("Iniciando transferencia de {} desde {} hacia {}",
                requestDto.getAmount(), requestDto.getSourceAccountNumber(), requestDto.getTargetAccountNumber());
        // Validar que no se transfiera a la misma cuenta
        if (requestDto.getSourceAccountNumber().equals(requestDto.getTargetAccountNumber())) {
            throw new IllegalArgumentException("La cuenta de origen y destino no pueden ser la misma");
        }
        // Registrar intento con estado PENDING en la base de datos de transferencias
        var transfer = mapper.toEntity(requestDto);
        var savedTransfer = repository.save(transfer);
        try {
            //PASO A: Debitar cuenta origen (isDeposit = false)
            log.info("Ejecutando débito en cuenta origen: {}", requestDto.getSourceAccountNumber());
            accountServiceRestClient.post()
                    .uri("/api/v1/accounts/{accountNumber}/transaction?amount={amount}&isDeposit=false",
                            requestDto.getSourceAccountNumber(), requestDto.getAmount())
                    .retrieve()
                    .toBodilessEntity();
            //PASO B: Abonar cuenta destino (isDeposit = true)
            log.info("Ejecutando abono en cuenta destino: {}", requestDto.getTargetAccountNumber());
            try {
                accountServiceRestClient.post()
                        .uri("/api/v1/accounts/{accountNumber}/transaction?amount={amount}&isDeposit=true",
                                requestDto.getTargetAccountNumber(), requestDto.getAmount())
                        .retrieve()
                        .toBodilessEntity();
            } catch (Exception e) {
                log.error("Fallo al abonar en cuenta destino. Iniciando compensación (reversión de débito)...", e);

                // COMPENSACIÓN: Devolver el dinero debitado a la cuenta origen
                accountServiceRestClient.post()
                        .uri("/api/v1/accounts/{accountNumber}/transaction?amount={amount}&isDeposit=true",
                                requestDto.getSourceAccountNumber(), requestDto.getAmount())
                        .retrieve()
                        .toBodilessEntity();
                savedTransfer.setStatus("FAILED_COMPENSATED");
                repository.save(savedTransfer);
                throw new RuntimeException("Transferencia fallida y revertida por error en destino: " + e.getMessage());
            }
            //Si todo sale bien, marcar la transferencia como exitosa
            savedTransfer.setStatus("SUCCESS");
            repository.save(savedTransfer);
            log.info("Transferencia completada exitosamente con ID: {}", savedTransfer.getId());
        } catch (HttpClientErrorException e) {
            // Si el account-service respondió con un 400 (ej. Fondos insuficientes o Cuenta no encontrada)
            log.warn("Error de cliente desde account-service: {}", e.getResponseBodyAsString());
            if (!"FAILED_COMPENSATED".equals(savedTransfer.getStatus()) && !"SUCCESS".equals(savedTransfer.getStatus())) {
                savedTransfer.setStatus("FAILED");
                repository.save(savedTransfer);
            }
            log.error("Error crítico en la ejecución de la transferencia: {}", e.getMessage());
            throw e;
        }
        return mapper.toResponseDto(savedTransfer);
    }
}
