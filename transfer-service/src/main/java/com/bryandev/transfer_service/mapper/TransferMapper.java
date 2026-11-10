package com.bryandev.transfer_service.mapper;

import com.bryandev.transfer_service.model.dto.request.TransferRequestDto;
import com.bryandev.transfer_service.model.dto.response.TransferResponseDto;
import com.bryandev.transfer_service.model.entities.Transfer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TransferMapper {
    public Transfer toEntity(TransferResponseDto responseDto) {
        if (responseDto==null) return null;
        return Transfer.builder()
                .id(responseDto.getId())
                .sourceAccountNumber(responseDto.getSourceAccountNumber())
                .targetAccountNumber(responseDto.getTargetAccountNumber())
                .amount(responseDto.getAmount())
                .status(responseDto.getStatus())
                .timestamp(responseDto.getTimestamp())
                .build();
    }

    public TransferResponseDto toResponseDto(Transfer entity) {
        if (entity==null) return null;
        return TransferResponseDto.builder()
                .id(entity.getId())
                .sourceAccountNumber(entity.getSourceAccountNumber())
                .targetAccountNumber(entity.getTargetAccountNumber())
                .amount(entity.getAmount())
                .status(entity.getStatus())
                .timestamp(entity.getTimestamp())
                .build();
    }

    public Transfer toEntity(TransferRequestDto requestDto) {
        if (requestDto==null) return null;
        return Transfer.builder()
                .sourceAccountNumber(requestDto.getSourceAccountNumber())
                .targetAccountNumber(requestDto.getTargetAccountNumber())
                .amount(requestDto.getAmount())
                .status("PENDING")
                .timestamp(LocalDateTime.now())
                .build();
    }
}
