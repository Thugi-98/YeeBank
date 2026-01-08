package com.example.yeebank.domain.transfer.service;

import com.example.yeebank.common.aop.annotation.RedisLock;
import com.example.yeebank.common.exception.CustomException;
import com.example.yeebank.common.exception.ErrorCode;
import com.example.yeebank.domain.account.entity.Account;
import com.example.yeebank.domain.account.repository.AccountRepository;
import com.example.yeebank.domain.account.service.AccountService;
import com.example.yeebank.domain.transfer.dto.dto.TransferDto;
import com.example.yeebank.domain.transfer.entity.Transfer;
import com.example.yeebank.domain.transfer.enums.TransferStatus;
import com.example.yeebank.domain.transfer.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransferService {
// - Properties
    private final TransferRepository transferRepository;

    // - Methods
    @Transactional
    public TransferDto deposit(Long toAccountId, Long amount, TransferStatus status, String failReason) {
        // - Create New Transfer
        Transfer newRecord = new Transfer(
                null,
                toAccountId,
                amount,
                status,
                failReason);

        // - Save New Transfer
        transferRepository.save(newRecord);

        // - Return Result
        return TransferDto.from(newRecord);
    }

    @Transactional
    public TransferDto withdrawal(Long fromAccountId, Long amount, TransferStatus status, String failReason) {
        // - Create New Transfer
        Transfer newRecord = new Transfer(
                fromAccountId,
                null,
                amount,
                status,
                failReason);

        // - Save New Transfer
        transferRepository.save(newRecord);

        // - Return Result
        return TransferDto.from(newRecord);
    }

    @Transactional
    public TransferDto remittance(Long fromAccountId, Long toAccountId, Long amount) {
        // - Create New Transfer
        Transfer newRecord = new Transfer(
                fromAccountId,
                toAccountId,
                amount,
                TransferStatus.SUCCESS,
                null);

        // - Save New Transfer
        transferRepository.save(newRecord);

        // - Return Result
        return TransferDto.from(newRecord);
    }

    public Page<TransferDto> GetAll(Pageable pageable) {
        return transferRepository.findAllfromDto(pageable);
    }
}
