package com.bryandev.account_service.service;

import com.bryandev.account_service.model.dtos.response.AccountResponseDto;

public interface AccountQueryService {
    AccountResponseDto getByAccountNumber(String accountNumber);
}
