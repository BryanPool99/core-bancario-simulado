package com.bryandev.transfer_service.repository;

import com.bryandev.transfer_service.model.entities.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
    // Método útil para buscar el historial de transferencias por cuenta de origen
    List<Transfer> findBySourceAccountNumber(String accountNumber);
}
