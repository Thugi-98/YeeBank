package com.example.yeebank.domain.user.service;

import com.example.yeebank.domain.user.dto.request.UserCreateRequestDto;
import com.example.yeebank.domain.user.dto.request.UserUpdateRequestDto;
import com.example.yeebank.domain.user.dto.response.UserCreateResponseDto;
import com.example.yeebank.domain.user.dto.response.UserGetAllResponseDto;
import com.example.yeebank.domain.user.dto.response.UserGetDetailResponseDto;
import com.example.yeebank.domain.user.dto.response.UserUpdateResponseDto;
import com.example.yeebank.domain.user.entity.User;
import com.example.yeebank.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 유저 회원가입(생성) 로직
     */
    @Transactional
    public UserCreateResponseDto createUser(UserCreateRequestDto requestDto) {
        User user = new User(
                requestDto.getName(),
                requestDto.getEmail(),
                requestDto.getPassword()

        );

        User saveUser = userRepository.save(user);

        UserCreateResponseDto responseDto = new UserCreateResponseDto(
                saveUser.getId(),
                saveUser.getName(),
                saveUser.getEmail(),
                saveUser.getCreatedAt(),
                saveUser.getUpdatedAt()
        );

        return responseDto;
    }

    /**
     * 유저 상세조회 로직
     */
    @Transactional(readOnly = true)
    public UserGetDetailResponseDto getDetailUser(Long userId) {
        User findUser = userRepository.findUserByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new RuntimeException(""));

        UserGetDetailResponseDto responseDto = new UserGetDetailResponseDto(
                findUser.getId(),
                findUser.getName(),
                findUser.getEmail(),
                findUser.getCreatedAt(),
                findUser.getUpdatedAt()
        );

        return responseDto;
    }

    /**
     * 유저 전체조회 로직
     */
    @Transactional(readOnly = true)
    public UserGetAllResponseDto getAllUser() {
        List<User> findUserList = userRepository.findUsersByIsDeletedFalse();

        Integer count = findUserList.size();

        List<UserGetAllResponseDto.UserListResponseDto> dtoList = new ArrayList<>();

        for (User user : findUserList) {
            UserGetAllResponseDto.UserListResponseDto dto = new UserGetAllResponseDto.UserListResponseDto(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getCreatedAt()
            );

            dtoList.add(dto);
        }

        UserGetAllResponseDto responseDto = new UserGetAllResponseDto(count, dtoList);

        return responseDto;
    }

    /**
     * 유저 정보 수정 로직
     */
    @Transactional
    public UserUpdateResponseDto updateUser(Long userId, UserUpdateRequestDto requestDto) {
        User findUser = userRepository.findUserByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new RuntimeException());

        findUser.updateUser(
                requestDto.getName(),
                requestDto.getEmail(),
                requestDto.getPassword()
        );

        UserUpdateResponseDto responseDto = new UserUpdateResponseDto(
                findUser.getId(),
                findUser.getName(),
                findUser.getEmail(),
                findUser.getCreatedAt(),
                findUser.getUpdatedAt()
        );

        return responseDto;
    }

    /**
     * 유저 삭제(소프트 딜리트) 로직
     */
    @Transactional
    public void deleteUser(Long userId) {
        User findUser = userRepository.findUserByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new RuntimeException(""));

        findUser.deleteUser();


    }

}
