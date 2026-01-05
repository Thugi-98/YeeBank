package com.example.yeebank.domain.account.service;

import com.example.yeebank.domain.account.dto.request.AccountCreateRequest;
import com.example.yeebank.domain.account.dto.response.AccountCreateResponse;
import com.example.yeebank.domain.account.dto.response.AccountDetailResponse;
import com.example.yeebank.domain.account.entity.Account;
import com.example.yeebank.domain.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;

    /// 계좌 생성
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

    /// 계좌 상세 조회
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


}
