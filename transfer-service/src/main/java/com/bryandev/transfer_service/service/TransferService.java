package com.bryandev.transfer_service.service;

import com.bryandev.transfer_service.model.dto.request.TransferRequestDto;
import com.bryandev.transfer_service.model.dto.response.TransferResponseDto;

public interface TransferService {
    TransferResponseDto executeTransfer(TransferRequestDto requestDto);
}
