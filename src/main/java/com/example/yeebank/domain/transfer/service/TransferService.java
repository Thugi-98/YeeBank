package com.example.yeebank.domain.transfer.service;

import com.example.yeebank.domain.account.entity.Account;
import com.example.yeebank.domain.account.repository.AccountRepository;
import com.example.yeebank.domain.account.service.AccountService;
import com.example.yeebank.domain.transfer.dto.dto.TransferDto;
import com.example.yeebank.domain.transfer.entity.Transfer;
import com.example.yeebank.domain.transfer.enums.TransferStatus;
import com.example.yeebank.domain.transfer.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.constraintvalidators.bv.time.past.PastValidatorForThaiBuddhistDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TransferService {
// - Properties
    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;

    // - Methods
    public TransferDto Deposit(Long toAccountId, Long amount) {
        // - Found Account
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new IllegalArgumentException("입금하려는 계좌가 없습니다."));

        // - Apply Balance Change
        // - accountService.Deposit(amount);

        // - Create New Transfer
        Transfer newRecord = new Transfer(
                null,
                toAccountId,
                amount,
                TransferStatus.SUCCESS,
                null,
                1L);

        // - Save New Transfer
        transferRepository.save(newRecord);

        // - Return Result
        return TransferDto.from(newRecord);
    }

    public TransferDto Withdrawal(Long fromAccountId, Long amount) {
        // - Found Account
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new IllegalArgumentException("출금하려는 계좌가 없습니다."));

        // - 의도적인 지연
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // - Apply Balance Change
        accountService.Withdrawal(fromAccount, amount);

        // - Create New Transfer
        Transfer newRecord = new Transfer(
                fromAccountId,
                null,
                amount,
                TransferStatus.SUCCESS,
                null,
                1L);

        // - Save New Transfer
        transferRepository.save(newRecord);

        // - Return Result
        return TransferDto.from(newRecord);
    }

    public TransferDto Remittance(Long fromAccountId, Long toAccountId, Long amount) {
        // - Found Account
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new IllegalArgumentException("입금하려는 계좌가 없습니다."));
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new IllegalArgumentException("출금하려는 계좌가 없습니다."));

        // - Apply Balance Change

        // - Create New Transfer
        Transfer newRecord = new Transfer(
                fromAccountId,
                toAccountId,
                amount,
                TransferStatus.SUCCESS,
                null,
                1L);

        // - Save New Transfer
        transferRepository.save(newRecord);

        // - Return Result
        return TransferDto.from(newRecord);
    }

    public Page<TransferDto> GetAll(Pageable pageable) {
        return transferRepository.findAllfromDto(pageable);
    }
}
