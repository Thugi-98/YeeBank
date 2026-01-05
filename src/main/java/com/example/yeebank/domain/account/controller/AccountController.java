package com.example.yeebank.domain.account.controller;

import com.example.yeebank.domain.account.dto.request.AccountCreateRequest;
import com.example.yeebank.domain.account.dto.response.AccountCreateResponse;
import com.example.yeebank.domain.account.dto.response.ApiResponse;
import com.example.yeebank.domain.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    /// 계좌 생성
    // 의존성 추가하고 @Valid 추가
    @PostMapping
    public ResponseEntity<ApiResponse<AccountCreateResponse>> createAccountApi(@RequestAttribute Long userId, @RequestBody AccountCreateRequest request) {
        log.info("계좌 생성 요청 - userId: {}, alias: {}", userId, request.getAlias());
        // 1. 서비스 호출
        AccountCreateResponse responseDto = accountService.createAccount(userId, request);

        // 2. Api 래퍼 생성
        ApiResponse<AccountCreateResponse> apiResponse = new ApiResponse<>(true, "계좌 개설 성공", responseDto);

        // 3. ResponseEntity 생성
        ResponseEntity<ApiResponse<AccountCreateResponse>> response = new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
        return response;
    }

}
