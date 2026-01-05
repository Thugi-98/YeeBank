package com.example.yeebank.domain.user.dto.response;

import com.example.yeebank.domain.user.dto.dto.PointDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserPointUsePointResponseDto {

    private final Long id;
    private final String name;
    private final long usePoint;
    private final long myPoint;

    public static UserPointUsePointResponseDto from(PointDto pointDto) {
        return new UserPointUsePointResponseDto(
                pointDto.getUserId(),
                pointDto.getName(),
                pointDto.getUsePoint(),
                pointDto.getMyPoint()
        );
    }
}
