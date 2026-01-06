package com.example.yeebank.domain.account.controller;

import com.example.yeebank.domain.account.dto.request.AccountCreateRequest;
import com.example.yeebank.domain.account.dto.request.AccountDeleteRequest;
import com.example.yeebank.domain.account.dto.request.AccountUpdateRequest;
import com.example.yeebank.domain.account.dto.response.*;
import com.example.yeebank.domain.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    /**
     * 계좌 생성
     */
    @PostMapping
    public ResponseEntity<ApiResponse<AccountCreateResponse>> createAccountApi(@RequestHeader("X-User-Id") Long userId,
                                                                               @Valid @RequestBody AccountCreateRequest request) {
        log.info("계좌 생성 요청 - userId: {}, alias: {}", userId, request.getAlias());
        // 1. 서비스 호출
        AccountCreateResponse responseDto = accountService.createAccount(userId, request);

        // 2. Api 래퍼 생성
        ApiResponse<AccountCreateResponse> apiResponse = new ApiResponse<>(true, "계좌 개설 성공", responseDto, LocalDateTime.now());

        // 3. ResponseEntity 생성
        ResponseEntity<ApiResponse<AccountCreateResponse>> response = new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
        return response;
    }

    /**
     * 계좌 상세조회
     */
    @GetMapping("/{accountId}")
    public ResponseEntity<ApiResponse<AccountDetailResponse>> getAccountApi(@PathVariable Long accountId,
                                                                            @RequestAttribute Long userId,
                                                                            @RequestHeader("X-Account-Password") String password) {
        log.info("계좌 단건조회 요청 - userId: {}, accountId: {}", userId, accountId);

        // 1. 서비스 호출
        AccountDetailResponse responseDto = accountService.getAccount(accountId, userId, password);

        // 2. Api 래퍼 생성
        ApiResponse<AccountDetailResponse> apiResponse = new ApiResponse<>(true, "계좌 정보 조회 성공", responseDto, LocalDateTime.now());

        // 3. ResponseEntity 생성
        ResponseEntity<ApiResponse<AccountDetailResponse>> response = new ResponseEntity<>(apiResponse, HttpStatus.OK);
        return response;
    }

    /**
     * 계좌 목록조회
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<AccountAllResponse>>> getAccountListApi(@RequestAttribute Long userId) {
        log.info("계좌 다건조회 요청 - userId: {}", userId);

        // 1. 서비스 호출
        List<AccountAllResponse> responseDto = accountService.getAccountList(userId);

        // 2. Api 래퍼 생성
        ApiResponse<List<AccountAllResponse>> apiResponse = new ApiResponse<>(true, "계좌 목록 조회 성공", responseDto, LocalDateTime.now());

        // 3. ResponseEntity 생성
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);


    }

    /**
     * 계좌 수정
     */
    @PutMapping("/{accountId}")
    public ResponseEntity<ApiResponse<AccountUpdateResponse>> updateAccountApi(@PathVariable Long accountId,
                                                                               @RequestAttribute Long userId,
                                                                               @Valid @RequestBody AccountUpdateRequest request
    ) {
        log.info("계좌 수정 요청 - accountId: {}, userId: {}, alias: {}",
                accountId, userId, request.getAlias());

        // 1. 서비스 호출
        AccountUpdateResponse responseDto =
                accountService.updateAccount(accountId, userId, request);

        // 2. Api 래퍼 생성
        ApiResponse<AccountUpdateResponse> apiResponse =
                new ApiResponse<>(true, "사용자 정보가 수정되었습니다.", responseDto, LocalDateTime.now());

        // 3. ResponseEntity 생성
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);


    }

    /**
     * 계좌 삭제(소프트 딜리트)
     */
    @DeleteMapping("/{accountId}")
    public ResponseEntity<ApiResponse<Void>> deleteAccountApi(@PathVariable Long accountId,
                                                              @RequestAttribute Long userId,
                                                              @Valid @RequestBody AccountDeleteRequest request) {
        log.info("계좌 삭제 요청 - accountId: {}, userId: {}, password: {}", accountId, userId, request.getPassword());

        // 1. 서비스 호출
        accountService.deleteAccount(accountId, userId, request.getPassword());

        // 2. Api 래퍼 생성
        ApiResponse<Void> apiResponse =
                new ApiResponse<>(true, "계좌 삭제 성공", null, LocalDateTime.now());

        // 3. ResponseEntity 생성 (200 OK)
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }


}



