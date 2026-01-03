package com.example.yeebank.domain.user.controller;

import com.example.yeebank.domain.user.dto.request.UserCreateRequestDto;
import com.example.yeebank.domain.user.dto.request.UserUpdateRequestDto;
import com.example.yeebank.domain.user.dto.response.UserCreateResponseDto;
import com.example.yeebank.domain.user.dto.response.UserGetAllResponseDto;
import com.example.yeebank.domain.user.dto.response.UserGetDetailResponseDto;
import com.example.yeebank.domain.user.dto.response.UserUpdateResponseDto;
import com.example.yeebank.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 유저 회원가입(생성) API
     */
    @PostMapping
    public ResponseEntity<UserCreateResponseDto> createUserApi(
            @RequestBody UserCreateRequestDto requestDto
    ) {
        UserCreateResponseDto responseDto = userService.createUser(requestDto);

        ResponseEntity<UserCreateResponseDto> response = new ResponseEntity<>(responseDto, HttpStatus.CREATED);

        return response;
    }

    /**
     * 유저 상세조회 API
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserGetDetailResponseDto> getDetailUserApi(
            @PathVariable("userId") Long userId
    ) {
        UserGetDetailResponseDto responseDto = userService.getDetailUser(userId);

        ResponseEntity<UserGetDetailResponseDto> response = new ResponseEntity<>(responseDto, HttpStatus.OK);

        return response;
    }

    /**
     * 유저 전체조회 API
     */
    @GetMapping
    public ResponseEntity<UserGetAllResponseDto> getAllUserApi(

    ) {
        UserGetAllResponseDto responseDto = userService.getAllUser();

        ResponseEntity<UserGetAllResponseDto> response = new ResponseEntity<>(responseDto, HttpStatus.OK);

        return response;
    }

    /**
     * 유저 정보 수정 API
     */
    @PutMapping("/{userId}")
    public ResponseEntity<UserUpdateResponseDto> updateUserApi(
            @PathVariable("userId") Long userId,
            @RequestBody UserUpdateRequestDto requestDto
    ) {
        UserUpdateResponseDto responseDto = userService.updateUser(userId, requestDto);

        ResponseEntity<UserUpdateResponseDto> response = new ResponseEntity<>(responseDto, HttpStatus.OK);

        return response;
    }

    /**
     * 유저 삭제(소프트 딜리트) API
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity deleteUserApi(
            @PathVariable("userId") Long userId
    ) {
        userService.deleteUser(userId);

        ResponseEntity response = new ResponseEntity<>(HttpStatus.OK);

        return response;
    }

}
