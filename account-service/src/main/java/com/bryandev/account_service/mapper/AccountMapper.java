package com.bryandev.account_service.mapper;

import com.bryandev.account_service.model.dtos.request.AccountRequestDto;
import com.bryandev.account_service.model.dtos.response.AccountResponseDto;
import com.bryandev.account_service.model.entities.Account;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountMapper {
    public Account toEntity(AccountResponseDto responseDto) {
        if (responseDto==null) return null;
        return Account.builder()
                .id(responseDto.getId())
                .accountNumber(responseDto.getAccountNumber())
                .ownerName(responseDto.getOwnerName())
                .balance(responseDto.getBalance())
                .build();
    }

    public Account toEntity(AccountRequestDto requestDto) {
        if (requestDto==null) return null;
        return Account.builder()
                .accountNumber(requestDto.getAccountNumber())
                .ownerName(requestDto.getOwnerName())
                .balance(requestDto.getInitialBalance()!=null ? requestDto.getInitialBalance():BigDecimal.ZERO)
                .build();
    }

    public AccountResponseDto toDto(Account entity) {
        if (entity==null) return null;
        return AccountResponseDto.builder()
                .id(entity.getId())
                .accountNumber(entity.getAccountNumber())
                .ownerName(entity.getOwnerName())
                .balance(entity.getBalance())
                .build();
    }
}
