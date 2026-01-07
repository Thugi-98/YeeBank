package com.example.yeebank.domain.user.dto.response;

import com.example.yeebank.domain.user.dto.dto.UserDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class UserGetDetailResponseDto {

    private final Long id;
    private final String name;
    private final String email;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static UserGetDetailResponseDto from(UserDto dto) {
        return new UserGetDetailResponseDto(
                dto.getId(),
                dto.getName(),
                dto.getEmail(),
                dto.getCreatedAt(),
                dto.getUpdatedAt()
        );
    }
}
