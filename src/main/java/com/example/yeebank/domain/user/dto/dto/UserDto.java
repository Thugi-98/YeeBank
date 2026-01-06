package com.example.yeebank.domain.user.dto.dto;

import com.example.yeebank.domain.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class UserDto {

    private final Long id;
    private final String name;
    private final String email;
    private final LocalDateTime createAt;
    private final LocalDateTime updatedAt;

    public static UserDto from(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

}
