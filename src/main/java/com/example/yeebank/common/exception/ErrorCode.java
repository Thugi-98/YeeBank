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
    USER_ALREADY_DELETED(HttpStatus.NOT_FOUND, "이미 비활성화 된 유저 입니다."),
    USER_DUPLICATE_USERNAME(HttpStatus.CONFLICT, "이미 존재하는 사용자명입니다."),
    USER_DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 사용중인 이메일입니다."),
    USER_ACCESS_DENIED(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    USER_CONFLICT_PASSWORD(HttpStatus.CONFLICT, "변경 하시려는 비밀번호는 변경 전과 같을 수 없습니다."),

    // 포인트 관련 ErrorCode
    POINT_USE_BELOW_MINIUM(HttpStatus.BAD_REQUEST, "사용 가능하신 최소 포인트는 1 포인트 이상 입니다."),
    POINT_USE_NOT_ENOUGH(HttpStatus.BAD_REQUEST, "보유하신 포인트가 부족합니다"),
    POINT_NOT_TODAY_ATTENDANCE(HttpStatus.FORBIDDEN, "출석체크를 진행할 수 없습니다."),
    POINT_DUPLICATE_ATTENDANCE(HttpStatus.CONFLICT, "오늘은 이미 출석체크 완료 되었습니다."),

    // 계좌 관련 ErrorCode
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "계좌가 존재하지 않습니다."),
    ACCOUNT_DUPLICATE_NUMBER(HttpStatus.CONFLICT, "이미 존재하는 계좌번호입니다."),

    // 거래 관련 ErrorCode
    TRANSFER_NOT_FOUND_SEND(HttpStatus.NOT_FOUND, "존재하지 않는 계좌로는 송금을 할 수 없습니다."),
//    TRANSFER_AMOUNT_MISS(HttpStatus.N , ""), //보유 잔액이 부족할때
    TRANSFER_CLIENT_MISS(HttpStatus.NOT_FOUND, "요청 클라이언트를 찾을 수 없습니다."),

    // Lock 관련 ErrorCode
    LOCK_ID_BADREQUEST(HttpStatus.BAD_REQUEST, "Lock을 시도할 AccountId가 잘못됐습니다."),
    LOCK_EXIST_REQUEST(HttpStatus.TOO_MANY_REQUESTS, "이미 실행중인 요청입니다.");

    private final HttpStatus status;
    private final String message;
}
