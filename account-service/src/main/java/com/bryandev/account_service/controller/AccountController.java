package com.bryandev.account_service.controller;

import com.bryandev.account_service.model.dtos.request.AccountRequestDto;
import com.bryandev.account_service.model.dtos.response.AccountResponseDto;
import com.bryandev.account_service.service.AccountQueryService;
import com.bryandev.account_service.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountQueryService accountQueryService;
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponseDto> createAccount(@Valid @RequestBody AccountRequestDto requestDto) {
        var created = accountService.createAccount(requestDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountResponseDto> getAccount(@PathVariable String accountNumber) {
        return ResponseEntity.ok(accountQueryService.getByAccountNumber(accountNumber));
    }

    @PostMapping("/{accountNumber}/transaction")
    public ResponseEntity<AccountResponseDto> processTransaction(
            @PathVariable String accountNumber,
            @RequestParam BigDecimal amount,
            @RequestParam boolean isDeposit) {
        var updated = accountService.updateBalance(accountNumber, amount, isDeposit);
        return ResponseEntity.ok(updated);
    }
}
