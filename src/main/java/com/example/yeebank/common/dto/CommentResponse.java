package com.example.yeebank.common.dto;

import java.time.LocalDateTime;

public class CommentResponse<T> {

    private final boolean success;
    private final String message;
    private final LocalDateTime timestamp;
    private final T data;

    private CommonResponse(boolean success, String message, T data, LocalDateTime timestamp) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = timestamp;
    }

    public static <T> CommonResponse<T> success(T data, String message) {
        return new CommonResponse<>(true, message , data, LocalDateTime.now());
    }

}
