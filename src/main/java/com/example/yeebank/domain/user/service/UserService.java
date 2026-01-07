package com.example.yeebank.domain.user.service;

import com.example.yeebank.common.exception.CustomException;
import com.example.yeebank.common.exception.ErrorCode;
import com.example.yeebank.domain.user.dto.dto.UserDto;
import com.example.yeebank.domain.user.dto.request.UserCreateRequestDto;
import com.example.yeebank.domain.user.dto.request.UserUpdateRequestDto;
import com.example.yeebank.domain.user.dto.response.UserCreateResponseDto;
import com.example.yeebank.domain.user.dto.response.UserGetAllResponseDto;
import com.example.yeebank.domain.user.dto.response.UserGetDetailResponseDto;
import com.example.yeebank.domain.user.dto.response.UserUpdateResponseDto;
import com.example.yeebank.domain.user.entity.User;
import com.example.yeebank.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 유저 회원가입(생성) 로직
     */
    @Transactional
    public UserCreateResponseDto createUser(UserCreateRequestDto requestDto) {

        Boolean existEmail = userRepository.existsByEmail(requestDto.getEmail());

        if (existEmail) {
            throw new CustomException(ErrorCode.USER_DUPLICATE_EMAIL);
        }

        String encodePassword = passwordEncoder.encode(requestDto.getPassword());

        User user = new User(
                requestDto.getName(),
                requestDto.getEmail(),
                encodePassword
        );

        User saveUser = userRepository.save(user);

        UserDto responseDto = UserDto.from(saveUser);

        return UserCreateResponseDto.from(responseDto);
    }

    /**
     * 유저 상세조회 로직
     */
    @Transactional(readOnly = true)
    public UserGetDetailResponseDto getDetailUser(Long userId) {
        User findUser = userRepository.findUserByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        UserDto responseDto = UserDto.from(findUser);

        return UserGetDetailResponseDto.from(responseDto);
    }

    /**
     * 유저 전체조회 로직
     */
    @Transactional(readOnly = true)
    public Page<UserGetAllResponseDto> getAllUser(Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<UserDto> findUserList = userRepository.findAllByIsDeletedFalse(pageable);

        Page<UserGetAllResponseDto> responseDtoPage = findUserList.map(UserGetAllResponseDto::from);

        return responseDtoPage;
    }

    /**
     * 유저 정보 수정 로직
     */
    @Transactional
    public UserUpdateResponseDto updateUser(Long userId, UserUpdateRequestDto requestDto) {
        User findUser = userRepository.findUserByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        findUser.updateUser(
                requestDto.getName(),
                requestDto.getEmail(),
                requestDto.getPassword()
        );

        if (!passwordEncoder.matches(requestDto.getPassword(), findUser.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        UserDto responseDto = UserDto.from(findUser);

        return UserUpdateResponseDto.from(responseDto);
    }

    /**
     * 유저 삭제(소프트 딜리트) 로직
     */
    @Transactional
    public void deleteUser(Long userId) {
        User findUser = userRepository.findUserByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        findUser.delete();
    }

}
