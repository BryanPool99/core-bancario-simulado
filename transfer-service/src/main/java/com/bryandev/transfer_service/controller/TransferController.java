package com.bryandev.transfer_service.controller;

import com.bryandev.transfer_service.model.dto.request.TransferRequestDto;
import com.bryandev.transfer_service.model.dto.response.TransferResponseDto;
import com.bryandev.transfer_service.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<TransferResponseDto> createTransfer(@Valid @RequestBody TransferRequestDto requestDto) {
        var responseDto = transferService.executeTransfer(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}
