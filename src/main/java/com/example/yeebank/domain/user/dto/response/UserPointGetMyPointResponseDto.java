package com.example.yeebank.domain.user.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserPointGetMyPointResponseDto {

    private final Long id;
    private final String name;
    private final long myPoint;
}
