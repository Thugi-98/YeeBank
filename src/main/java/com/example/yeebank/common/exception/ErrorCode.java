package com.example.yeebank.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Valid 검증 ErrorCode
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "요청 값이 올바르지 않습니다: "),

    // 인증,권한 관련 ErrorCode
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 올바르지 않습니다."),

    // 유저 관련 ErrorCode
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."),
    USER_DUPLICATE_USERNAME(HttpStatus.CONFLICT, "이미 존재하는 사용자명입니다."),
    USER_DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 사용중인 이메일입니다."),
    USER_ACCESS_DENIED(HttpStatus.FORBIDDEN, "권한이 없습니다."),

    // 계좌 관련 ErrorCode
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "계좌가 존재하지 않습니다."),
    ACCOUNT_DUPLICATE_NUMBER(HttpStatus.CONFLICT, "이미 존재하는 계좌번호입니다."),

    // 거래 관련 ErrorCode
    TRANSFER_NOT_FOUND_SEND(HttpStatus.NOT_FOUND, "존재하지 않는 계좌로는 송금을 할 수 없습니다."),
//    TRANSFER_AMOUNT_MISS(HttpStatus.), 보유 잔액이 부족할때
    TRANSFER_CLIENT_MISS(HttpStatus.NOT_FOUND, "요청 클라이언트를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;
}
