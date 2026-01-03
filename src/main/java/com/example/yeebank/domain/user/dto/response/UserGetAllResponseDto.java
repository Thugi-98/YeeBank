package com.example.yeebank.domain.user.dto.response;

import com.example.yeebank.domain.user.dto.dto.UserDto;
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

        public static UserListResponseDto from(UserDto dto) {
            return new UserListResponseDto(
                    dto.getId(),
                    dto.getName(),
                    dto.getEmail(),
                    dto.getCreateAt()
            );
        }
    }
}
