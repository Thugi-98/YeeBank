package com.example.yeebank.common.dto;

import com.example.yeebank.common.exception.ErrorCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommonResponse<T> {

    private final boolean success;         // 성공 여부
    private final String message;          // 성공시 요청이 성공했습니다. , 실패시 error message
    private final LocalDateTime timestamp; // 실행 시간
    private final T data;                  // 성공 시 data , 실패시 null

    public CommonResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    // 성공시 공용 응답 객체
    public static <T> CommonResponse<T> success(T data, String message) {
        return new CommonResponse<>(true, message, data);
    }

    // 실패시 공용 응답 객체
    public static <T> CommonResponse<T> fail(ErrorCode errorCode) {
        return new CommonResponse<>(false, errorCode.getMessage(), null);
    }

    public static <T> CommonResponse<T> fail(String message) {
        return new CommonResponse<>(false, message, null);
    }

    // 메세지를 직접 입력하는 경우
    public static <T> CommonResponse<T> fail(ErrorCode errorCode, String message) {
        return new CommonResponse<>(
                false,
                message,
                null
        );
    }
}
