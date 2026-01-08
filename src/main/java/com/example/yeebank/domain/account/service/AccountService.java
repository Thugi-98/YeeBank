package com.example.yeebank.domain.account.service;

import com.example.yeebank.common.aop.annotation.RedisLock;
import com.example.yeebank.common.dto.PageResponse;
import com.example.yeebank.common.exception.CustomException;
import com.example.yeebank.common.exception.ErrorCode;
import com.example.yeebank.common.security.CustomUserDetails;
import com.example.yeebank.domain.account.dto.request.AccountCreateRequest;
import com.example.yeebank.domain.account.dto.request.AccountUpdateRequest;
import com.example.yeebank.domain.account.dto.response.AccountAllResponse;
import com.example.yeebank.domain.account.dto.response.AccountCreateResponse;
import com.example.yeebank.domain.account.dto.response.AccountDetailResponse;
import com.example.yeebank.domain.account.dto.response.AccountUpdateResponse;
import com.example.yeebank.domain.account.entity.Account;
import com.example.yeebank.domain.account.repository.AccountRepository;
import com.example.yeebank.domain.transfer.enums.TransferStatus;
import com.example.yeebank.domain.transfer.service.TransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;
    private final TransferService transferService;

    /**
     * 계좌 생성
     */
    @Transactional
    public AccountCreateResponse createAccount(CustomUserDetails user, AccountCreateRequest request) {

        // 1. 계좌 별칭 중복 검사(소프트 딜리트된 계좌는 제외)
        if (accountRepository.existsByUserIdAndAliasAndIsDeletedFalse(user.getUserId(), request.getAlias())) {
            throw new CustomException(ErrorCode.ACCOUNT_DUPLICATE_ALIAS);
        }
        // 2. 계좌번호 생성
        String accountNumber = generateAccountNumber();

       // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // Entity 생성
        Account account = Account.builder()
                .userId(user.getUserId())
                .accountNumber(accountNumber)
                .password(encodedPassword)
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
    public AccountDetailResponse getAccount(Long accountId, CustomUserDetails user, String password) {
        // 1. 계좌 조회 (소프트 딜리트 제외)
        Account account = accountRepository.findByIdAndIsDeletedFalse(accountId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        // 2. 본인 계좌인지 확인
        if (!account.getUserId().equals(user.getUserId())) {
            throw new CustomException(ErrorCode.ACCOUNT_ACCESS_DENIED);
        }

        // 3. 비밀번호 일치여부 검증
        if (!passwordEncoder.matches(password,account.getPassword())) {
            throw new CustomException(ErrorCode.ACCOUNT_INVALID_PASSWORD);
        }

        // 4. Response DTO로 변환 (정적 팩토리 메서드)
        return AccountDetailResponse.from(account);
    }

    /**
     * 계좌 목록조회 -> 페이징 적용
     */
    public PageResponse<AccountAllResponse> getAccountList(CustomUserDetails user, int page, int size) {
        log.info("계좌 목록 조회 - user: {}, page: {}, size: {}", user, page, size);
        // 1. Pageable 생성 (최신순 정렬)
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        // 2. 페이징 조회
        Page<Account> accountPage = accountRepository.findAllByUserIdAndIsDeletedFalse(user.getUserId(), pageable);

        // 3. Response DTO로 변환
        Page<AccountAllResponse> responsePage = accountPage.map(AccountAllResponse::from);
        return PageResponse.from(responsePage);

    }

    /**
     * 계좌 수정
     */
    @Transactional
    public AccountUpdateResponse updateAccount(Long accountId, CustomUserDetails user, AccountUpdateRequest request) {
        log.info("계좌 수정 - accountId: {}, user: {}, newAlias: {}", accountId, user, request.getAlias());
        // 1. 계좌 조회
        Account account = accountRepository.findByIdAndIsDeletedFalse(accountId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        // 2. 본인 계좌인지 확인
        if (!account.getUserId().equals(user.getUserId())) {
            throw new CustomException(ErrorCode.ACCOUNT_ACCESS_DENIED);
        }

        // 3. 별칭 중복 검사 (자기 자신 제외)
        if (accountRepository.existsByUserIdAndAliasAndIsDeletedFalse(user.getUserId(), request.getAlias())) {
            // 같은 별칭으로 수정하는 경우는 허용
            if (!account.getAlias().equals(request.getAlias())) {
                throw new CustomException(ErrorCode.ACCOUNT_DUPLICATE_ALIAS);
            }
        }
        // 4. 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(),account.getPassword())) {
            throw new CustomException(ErrorCode.ACCOUNT_INVALID_PASSWORD);
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
    public void deleteAccount(Long accountId,CustomUserDetails user , String password) {
        // 1. 계좌 조회
        Account account = accountRepository.findByIdAndIsDeletedFalse(accountId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        // 2. 본인 계좌인지 확인
        if (!account.getUserId().equals(user.getUserId())) {
            throw new CustomException(ErrorCode.ACCOUNT_ACCESS_DENIED);
        }

        // 3. 비밀번호 일치여부 검증
        if (!passwordEncoder.matches(password,account.getPassword())) {
            throw new CustomException(ErrorCode.ACCOUNT_INVALID_PASSWORD);
        }

        // 4. 잔액 확인
        if (account.getBalance() > 0) {
            throw new CustomException(ErrorCode.ACCOUNT_BALANCE_NOT_EMPTY);
        }

        // 5. 소프트 딜리트
        account.delete();

    }

    // - 입금
    @RedisLock(key = "lock:account:")
    @Transactional
    public AccountDetailResponse deposit(Long toAccountId, Long amount) {
        TransferStatus status = TransferStatus.SUCCESS;
        String failReason = null;

        try {
            Account toAccount = accountRepository.findById(toAccountId)
                    .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));
            toAccount.setBalance(toAccount.getBalance() + amount);
            accountRepository.save(toAccount);
            return AccountDetailResponse.from(toAccount);
        } catch (RuntimeException e) {
            status = TransferStatus.FAIL;
            failReason = e.getMessage();
            throw e;
        } finally {
            transferService.deposit(toAccountId, amount, status, failReason);
        }
    }
    @RedisLock(key = "lock:account:")
    @Transactional
    public AccountDetailResponse withdrawal(Long fromAccountId, Long amount) {
        TransferStatus status = TransferStatus.SUCCESS;
        String failReason = null;

        try {
            Account fromAccount = accountRepository.findById(fromAccountId)
                    .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));
            long money = fromAccount.getBalance();
            if (money >= amount) {
                fromAccount.setBalance(fromAccount.getBalance() - amount);
                accountRepository.save(fromAccount);
            } else {
                
            }
            return AccountDetailResponse.from(fromAccount);
        } catch (RuntimeException e) {
            status = TransferStatus.FAIL;
            failReason = e.getMessage();
            throw e;
        } finally {
            transferService.withdrawal(fromAccountId, amount, status, failReason);
        }
    }
}
