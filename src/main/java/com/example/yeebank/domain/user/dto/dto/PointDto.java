package com.example.yeebank.domain.user.dto.dto;

import com.example.yeebank.domain.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class PointDto {

    private final Long userId;
    private final String name;
    private final long earnPoint;
    private final long myPoint;
    private final long usePoint;
    private final LocalDate today;

    public static PointDto from(User user, long earnPoint, long usePoint) {
        return new PointDto(
                user.getId(),
                user.getName(),
                earnPoint,
                user.getMyPoint(),
                usePoint,
                LocalDate.now()
        );
    }

}
