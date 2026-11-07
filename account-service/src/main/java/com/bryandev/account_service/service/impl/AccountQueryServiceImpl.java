package com.bryandev.account_service.service.impl;

import com.bryandev.account_service.exception.AccountNotFoundException;
import com.bryandev.account_service.mapper.AccountMapper;
import com.bryandev.account_service.model.dtos.response.AccountResponseDto;
import com.bryandev.account_service.repository.AccountRepository;
import com.bryandev.account_service.service.AccountQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountQueryServiceImpl implements AccountQueryService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    @Transactional(readOnly = true)
    public AccountResponseDto getByAccountNumber(String accountNumber) {
        log.info("Inicio de getByAccountNumber - accountNumber: {}", accountNumber);
        var accountEntityFind = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> {
                    log.warn("Cuenta no encontrada - accountNumber: {}", accountNumber);
                    return new AccountNotFoundException("Cuenta no encontrada con numero " + accountNumber);
                });
        log.info("Cuenta encontrada - id: {}, accountNumber: {}", accountEntityFind.getId(),
                accountEntityFind.getAccountNumber());
        log.info("Fin de getByAccountNumber - accountNumber: {}", accountNumber);
        return accountMapper.toDto(accountEntityFind);
    }
}
