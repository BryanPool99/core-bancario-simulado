package com.bryandev.transfer_service.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccountClientWrapper {

    private final RestClient accountServiceRestClient;


    @CircuitBreaker(name = "accountServiceCB", fallbackMethod = "fallbackTransaction")
    @Retry(name = "accountServiceRetry")
    public void executeAccountTransaction(String accountNumber, BigDecimal amount, boolean isDeposit) {
        log.info("Llamando a account-service - Cuenta: {}, Monto: {}, Es depósito: {}",
                accountNumber, amount, isDeposit);

        accountServiceRestClient.post()
                .uri("/api/v1/accounts/{accountNumber}/transaction?amount={amount}&isDeposit={isDeposit}",
                        accountNumber, amount, isDeposit)
                .retrieve()
                .toBodilessEntity();
    }

    // Método Fallback que se activa si el account-service falla o el circuito está abierto
    private void fallbackTransaction(String accountNumber, BigDecimal amount, boolean isDeposit, Exception ex) {
        log.error("Circuit Breaker activado para account-service al operar la cuenta {}. Motivo: {}",
                accountNumber, ex.getMessage());

        // Si el error original fue una excepción de cliente (como 400 Bad Request por Fondos Insuficientes),
        // debemos relanzarla para que no se confunda con un fallo del circuito.
        if (ex instanceof org.springframework.web.client.HttpClientErrorException) {
            throw (org.springframework.web.client.HttpClientErrorException) ex;
        }

        throw new RuntimeException("El servicio de cuentas no está disponible temporalmente. Intente más tarde.");
    }
}
