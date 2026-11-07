package com.bryandev.account_service.service;

import com.bryandev.account_service.model.dtos.request.AccountRequestDto;
import com.bryandev.account_service.model.dtos.response.AccountResponseDto;

import java.math.BigDecimal;

public interface AccountService {
    AccountResponseDto createAccount(AccountRequestDto account);

    AccountResponseDto updateBalance(String accountNumber, BigDecimal amount, boolean isDeposit);
}
