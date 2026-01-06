package com.example.yeebank.domain.account.service;

import com.example.yeebank.domain.account.dto.request.AccountCreateRequest;
import com.example.yeebank.domain.account.dto.request.AccountUpdateRequest;
import com.example.yeebank.domain.account.dto.response.AccountAllResponse;
import com.example.yeebank.domain.account.dto.response.AccountCreateResponse;
import com.example.yeebank.domain.account.dto.response.AccountDetailResponse;
import com.example.yeebank.domain.account.dto.response.AccountUpdateResponse;
import com.example.yeebank.domain.account.entity.Account;
import com.example.yeebank.domain.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;

    /**
     * 계좌 생성
     */
    @Transactional
    public AccountCreateResponse createAccount(Long userId, AccountCreateRequest request) {

        // 1. 계좌 별칭 중복 검사(소프트 딜리트된 계좌는 제외)
        if (accountRepository.existsByUserIdAndAliasAndIsDeletedFalse(userId, request.getAlias())) {
            throw new RuntimeException("이미 존재하는 계좌 별칭입니다."); // 나중에 수정 (예외처리부분)
        }
        // 2. 계좌번호 생성
        String accountNumber = generateAccountNumber();

        // 2. Entity 생성
        Account account = Account.builder()
                .userId(userId)
                .accountNumber(accountNumber)
                .password(request.getPassword())
                .alias(request.getAlias())
                .balance(0L)
                .build();

        // 3. 저장(레포지토리 활용)
        Account savedAccount = accountRepository.save(account);

        // 4. Response DTO로 변환 (정적 팩토리 메서드)
        return AccountCreateResponse.from(savedAccount);
    }

    // 계좌번호 생성 메서드
    private String generateAccountNumber() {
        Random random = new Random();
        StringBuilder accountNumber = new StringBuilder();

        // 16자리 랜덤 숫자 생성
        for (int i = 0; i < 16; i++) {
            accountNumber.append(random.nextInt(10));
        }

        return accountNumber.toString();
    }

    /**
     * 계좌 상세조회
     */
    public AccountDetailResponse getAccount(Long accountId, Long userId, Long password) {
        // 1. 계좌 조회 (소프트 딜리트 제외)
        Account account = accountRepository.findByIdAndIsDeletedFalse(accountId)
                .orElseThrow(() -> new RuntimeException("계좌를 찾을 수 없습니다"));

        // 2. 본인 계좌인지 확인
        if (!account.getUserId().equals(userId)) {
            throw new RuntimeException("본인의 계좌만 조회할 수 있습니다.");
        }

        // 3. 비밀번호 일치여부 검증
        if (!account.getPassword().equals(password)) {
            throw new RuntimeException("계좌 비밀번호가 일치하지 않습니다.");
        }

        // 4. Response DTO로 변환 (정적 팩토리 메서드)
        return AccountDetailResponse.from(account);
    }

    /**
     * 계좌 목록조회
     */
    public List<AccountAllResponse> getAccountList(Long userId) {
        log.info("계좌 목록 조회 - userId: {}", userId);

        // 1. 계좌 목록 조회
        List<Account> accountList = accountRepository.findAllByUserIdAndIsDeletedFalse(userId);

        // 2. Response DTO로 변환
        return accountList.stream()
                .map(AccountAllResponse::from)
                .toList();
    }

    /**
     * 계좌 수정
     */
    @Transactional
    public AccountUpdateResponse updateAccount(Long accountId, Long userId, AccountUpdateRequest request) {
        log.info("계좌 수정 - accountId: {}, userId: {}, newAlias: {}", accountId, userId, request.getAlias());
        // 1. 계좌 조회
        Account account = accountRepository.findByIdAndIsDeletedFalse(accountId)
                .orElseThrow(() -> new RuntimeException("계좌를 찾을 수 없습니다"));

        // 2. 본인 계좌인지 확인
        if (!account.getUserId().equals(userId)) {
            throw new RuntimeException("본인의 계좌만 수정할 수 있습니다.");
        }

        // 3. 별칭 중복 검사 (자기 자신 제외)
        if (accountRepository.existsByUserIdAndAliasAndIsDeletedFalse(userId, request.getAlias())) {
            // 같은 별칭으로 수정하는 경우는 허용
            if (!account.getAlias().equals(request.getAlias())) {
                throw new RuntimeException("이미 존재하는 계좌 별칭입니다.");
            }
        }
        // 4. 비밀번호 검증
        if (!account.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("계좌 비밀번호가 일치하지 않습니다.");
        }

        // 5. 별칭 업데이트
        account.updateAlias(request.getAlias());

        // 6. Response DTO로 변환
        return AccountUpdateResponse.from(account);
    }

    /**
     * 계좌 삭제(소프트 딜리트)
     */
    @Transactional
    public void deleteAccount(Long accountId, Long userId, Long password) {
        // 1. 계좌 조회
        Account account = accountRepository.findByIdAndIsDeletedFalse(accountId)
                .orElseThrow(() -> new RuntimeException("계좌를 찾을 수 없습니다"));

        // 2. 본인 계좌인지 확인
        if (!account.getUserId().equals(userId)) {
            throw new RuntimeException("본인의 계좌만 삭제할 수 있습니다.");
        }

        // 3. 비밀번호 일치여부 검증
        if (!account.getPassword().equals(password)) {
            throw new RuntimeException("계좌 비밀번호가 일치하지 않습니다.");
        }

        // 4. 잔액 확인
        if (account.getBalance() > 0) {
            throw new RuntimeException("잔액이 있는 계좌는 삭제할 수 없습니다.");
        }

        // 5. 소프트 딜리트
        account.softDelete();

    }


}
