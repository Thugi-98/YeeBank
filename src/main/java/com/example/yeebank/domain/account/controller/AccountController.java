package com.example.yeebank.domain.account.controller;

import com.example.yeebank.common.dto.CommonResponse;
import com.example.yeebank.common.dto.PageResponse;
import com.example.yeebank.domain.account.dto.request.AccountCreateRequest;
import com.example.yeebank.domain.account.dto.request.AccountDeleteRequest;
import com.example.yeebank.domain.account.dto.request.AccountUpdateRequest;
import com.example.yeebank.domain.account.dto.response.*;
import com.example.yeebank.domain.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    /**
     * 계좌 생성
     */
    @PostMapping
    public ResponseEntity<ApiResponse<AccountCreateResponse>> createAccountApi(@RequestHeader("X-User-Id") Long userId,
                                                                               @Valid @RequestBody AccountCreateRequest request) {
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

        // 1. 서비스 호출
        AccountDetailResponse responseDto = accountService.getAccount(accountId, userId, password);

        // 2. Api 래퍼 생성
        ApiResponse<AccountDetailResponse> apiResponse = new ApiResponse<>(true, "계좌 정보 조회 성공", responseDto, LocalDateTime.now());

        // 3. ResponseEntity 생성
        ResponseEntity<ApiResponse<AccountDetailResponse>> response = new ResponseEntity<>(apiResponse, HttpStatus.OK);
        return response;
    }

    /**
     * 계좌 목록조회 -> 페이징 적용
     */
    @GetMapping
    public ResponseEntity<CommonResponse<PageResponse<AccountAllResponse>>> getAccountListApi(@RequestAttribute Long userId,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size) {

        // 1. 서비스 호출(페이징)
        PageResponse<AccountAllResponse> accountAllResponsePage = accountService.getAccountList(userId, page, size);

        // 2. 공통 응답 생성
        CommonResponse<PageResponse<AccountAllResponse>> commonResponse = CommonResponse.success(accountAllResponsePage,"계좌 목록 조회 성공.");

        // 3. 응답 반환
        return ResponseEntity.ok(commonResponse);
    }

    /**
     * 계좌 수정
     */
    @PutMapping("/{accountId}")
    public ResponseEntity<ApiResponse<AccountUpdateResponse>> updateAccountApi(@PathVariable Long accountId,
                                                                               @RequestAttribute Long userId,
                                                                               @Valid @RequestBody AccountUpdateRequest request
    ) {

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

        // 1. 서비스 호출
        accountService.deleteAccount(accountId, userId, request.getPassword());

        // 2. Api 래퍼 생성
        ApiResponse<Void> apiResponse =
                new ApiResponse<>(true, "계좌 삭제 성공", null, LocalDateTime.now());

        // 3. ResponseEntity 생성 (200 OK)
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    // - 입금
    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<CommonResponse<AccountDetailResponse>> deposit(
            @PathVariable Long accountId,
            @RequestParam Long amount) {
        AccountDetailResponse result = accountService.deposit(accountId, amount);
        return ResponseEntity.ok(CommonResponse.success(result, "입금 성공"));
    }
    // - 출금
    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<CommonResponse<AccountDetailResponse>> withdraw(
            @PathVariable Long accountId,
            @RequestParam Long amount) {
        accountService.withdrawal(accountId, amount);
        AccountDetailResponse result = accountService.withdrawal(accountId, amount);
        return ResponseEntity.ok(CommonResponse.success(result, "출금 성공"));
    }
}



