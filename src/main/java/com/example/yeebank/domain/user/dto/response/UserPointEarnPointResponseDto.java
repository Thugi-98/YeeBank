package com.example.yeebank.domain.user.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserPointEarnPointResponseDto {

    private final Long userId;
    private final String name;
    private final long earnPoint;
    private final long myPoint;

}
