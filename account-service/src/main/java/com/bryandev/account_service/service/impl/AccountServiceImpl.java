package com.bryandev.account_service.service.impl;

import com.bryandev.account_service.exception.AccountNotFoundException;
import com.bryandev.account_service.exception.InsufficientFundsException;
import com.bryandev.account_service.mapper.AccountMapper;
import com.bryandev.account_service.model.dtos.request.AccountRequestDto;
import com.bryandev.account_service.model.dtos.response.AccountResponseDto;
import com.bryandev.account_service.repository.AccountRepository;
import com.bryandev.account_service.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    @Transactional
    public AccountResponseDto createAccount(AccountRequestDto requestDto) {
        log.info("Inicio de método createAccount");
        var newAccountEntity = accountMapper.toEntity(requestDto);
        log.debug("Entidad mapeada desde DTO: {}", newAccountEntity);
        var savedAccount = accountRepository.save(newAccountEntity);
        log.info("Cuenta creada exitosamente con id: {}", savedAccount.getId());
        log.info("Fin de createAccount - id: {}", savedAccount.getId());
        return accountMapper.toDto(savedAccount);
    }

    @Override
    @Transactional
    public AccountResponseDto updateBalance(String accountNumber, BigDecimal amount, boolean isDeposit) {
        log.info("Inicio de updateBalance - accountNumber: {}, amount: {}, isDeposit: {}",
                accountNumber, amount, isDeposit);
        if (amount==null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }

        var account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException("Cuenta no encontrada con número: " + accountNumber));

        BigDecimal previousBalance = account.getBalance();
        if (isDeposit) {
            account.setBalance(previousBalance.add(amount));
        } else {
            if (previousBalance.compareTo(amount) < 0) {
                log.warn("Fondos insuficientes - accountNumber: {}, saldoActual: {}, amountSolicitado: {}",
                        accountNumber, previousBalance, amount);
                throw new InsufficientFundsException("Fondos insuficientes. Saldo actual: " + previousBalance);
            }
            account.setBalance(previousBalance.subtract(amount));
        }

        log.info("Balance actualizado - accountNumber: {}, saldoAnterior: {}, saldoNuevo: {}, isDeposit: {}",
                accountNumber, previousBalance, account.getBalance(), isDeposit);

        var updatedAccount = accountRepository.save(account);

        log.info("Fin de updateBalance - accountNumber: {}", accountNumber);
        return accountMapper.toDto(updatedAccount);
    }
}
