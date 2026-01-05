package com.example.yeebank.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class GlobalExceptionHandler {


    @ExceptionHandler(CustomException.class)
    public ResponseEntity<> handleException(CustomException e) {}
}
