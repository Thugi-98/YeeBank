package com.example.yeebank.domain.account.controller;

import com.example.yeebank.domain.account.dto.request.AccountCreateRequest;
import com.example.yeebank.domain.account.dto.request.AccountDetailRequest;
import com.example.yeebank.domain.account.dto.response.AccountCreateResponse;
import com.example.yeebank.domain.account.dto.response.AccountDetailResponse;
import com.example.yeebank.domain.account.dto.response.ApiResponse;
import com.example.yeebank.domain.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    /// 계좌 생성
    @PostMapping
    public ResponseEntity<ApiResponse<AccountCreateResponse>> createAccountApi(@RequestAttribute Long userId, @Valid @RequestBody AccountCreateRequest request) {
        log.info("계좌 생성 요청 - userId: {}, alias: {}", userId, request.getAlias());
        // 1. 서비스 호출
        AccountCreateResponse responseDto = accountService.createAccount(userId, request);

        // 2. Api 래퍼 생성
        ApiResponse<AccountCreateResponse> apiResponse = new ApiResponse<>(true, "계좌 개설 성공", responseDto, LocalDateTime.now());

        // 3. ResponseEntity 생성
        ResponseEntity<ApiResponse<AccountCreateResponse>> response = new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
        return response;
    }

    /// 계좌 단건조회
    @GetMapping("/{accountId}")
    public ResponseEntity<ApiResponse<AccountDetailResponse>> getAccountApi(@PathVariable Long accountId, @RequestAttribute Long userId, @Valid @RequestBody AccountDetailRequest request) {
        log.info("계좌 단건 조회 요청 - userId: {}, accountId: {}", userId, accountId);

        // 1. 서비스 호출
        AccountDetailResponse responseDto = accountService.getAccount(accountId, userId, request.getPassword());

        // 2. Api 래퍼 생성
        ApiResponse<AccountDetailResponse> apiResponse = new ApiResponse<>(true, "계좌 정보 조회 성공", responseDto, LocalDateTime.now());

        // 3. ResponseEntity 생성
        ResponseEntity<ApiResponse<AccountDetailResponse>> response = new ResponseEntity<>(apiResponse, HttpStatus.OK);
        return response;
    }

}
