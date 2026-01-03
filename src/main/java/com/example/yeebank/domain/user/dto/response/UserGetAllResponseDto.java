package com.example.yeebank.domain.user.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class UserGetAllResponseDto {

    private final Integer count;
    private final List<UserListResponseDto> userList;


    @Getter
    @RequiredArgsConstructor
    public static class UserListResponseDto {
        private final Long id;
        private final String name;
        private final String email;
        private final LocalDateTime createdAt;
    }
}
