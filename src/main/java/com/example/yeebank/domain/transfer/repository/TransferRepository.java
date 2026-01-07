package com.example.yeebank.domain.transfer.repository;

import com.example.yeebank.domain.transfer.dto.dto.TransferDto;
import com.example.yeebank.domain.transfer.entity.Transfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
// - Methods
    @Query("SELECT t FROM Transfer t ORDER BY t.createdAt")
    Page<TransferDto> findAllfromDto(Pageable pageable);
}
