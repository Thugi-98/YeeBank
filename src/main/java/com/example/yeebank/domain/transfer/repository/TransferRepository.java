package com.example.yeebank.domain.transfer.repository;

import com.example.yeebank.domain.transfer.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
}
