package com.example.yeebank.domain.user.dto.response;

import com.example.yeebank.domain.user.dto.dto.PointDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserPointGetMyPointResponseDto {

    private final Long id;
    private final String name;
    private final long myPoint;

    public static UserPointGetMyPointResponseDto from(PointDto pointDto) {
        return new UserPointGetMyPointResponseDto(
                pointDto.getUserId(),
                pointDto.getName(),
                pointDto.getMyPoint()
        );
    }
}
