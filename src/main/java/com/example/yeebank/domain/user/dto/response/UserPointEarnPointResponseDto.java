package com.example.yeebank.domain.user.dto.response;

import com.example.yeebank.domain.user.dto.dto.PointDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserPointEarnPointResponseDto {

    private final Long userId;
    private final String name;
    private final long earnPoint;
    private final long myPoint;

    public static UserPointEarnPointResponseDto from(PointDto pointDto) {
        return new UserPointEarnPointResponseDto(
                pointDto.getUserId(),
                pointDto.getName(),
                pointDto.getEarnPoint(),
                pointDto.getMyPoint()
        );
    }
}
