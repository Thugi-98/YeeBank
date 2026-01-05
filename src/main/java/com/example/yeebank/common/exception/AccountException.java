package com.example.yeebank.common.exception;

public class AccountException extends CustomException {
    public AccountException(ErrorCode errorCode) {
        super(errorCode);
    }
}
