package com.example.yeebank.domain.user.controller;

import com.example.yeebank.common.dto.CommonResponse;
import com.example.yeebank.common.dto.PageResponse;
import com.example.yeebank.common.security.CustomUserDetails;
import com.example.yeebank.domain.account.dto.response.AccountAllResponse;
import com.example.yeebank.domain.user.dto.request.UserCreateRequestDto;
import com.example.yeebank.domain.user.dto.request.UserUpdateRequestDto;
import com.example.yeebank.domain.user.dto.response.UserCreateResponseDto;
import com.example.yeebank.domain.user.dto.response.UserGetAllResponseDto;
import com.example.yeebank.domain.user.dto.response.UserGetDetailResponseDto;
import com.example.yeebank.domain.user.dto.response.UserUpdateResponseDto;
import com.example.yeebank.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<CommonResponse<UserCreateResponseDto>> createUserApi(
            @Valid @RequestBody UserCreateRequestDto requestDto
    ) {
        UserCreateResponseDto responseDto = userService.createUser(requestDto);

        CommonResponse<UserCreateResponseDto> commonResponse = new CommonResponse<>(true, "회원가입 완료", responseDto);

        return new ResponseEntity<>(commonResponse, HttpStatus.CREATED);
    }

    /**
     * 유저 상세조회 API
     */
    @GetMapping("/{userId}")
    public ResponseEntity<CommonResponse<UserGetDetailResponseDto>> getDetailUserApi(
            @PathVariable("userId") Long userId
    ) {
        UserGetDetailResponseDto responseDto = userService.getDetailUser(userId);

        CommonResponse<UserGetDetailResponseDto> commonResponse = new CommonResponse<>(true, "사용자 정보 조회 성공", responseDto);

        return new ResponseEntity<>(commonResponse, HttpStatus.CREATED);
    }

    /**
     * 유저 전체조회 API
     */
    @GetMapping
    public ResponseEntity<CommonResponse<PageResponse<UserGetAllResponseDto>>> getAllUserApi(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size

    ) {
        PageResponse<UserGetAllResponseDto> responseDto = userService.getAllUser(page, size);

        CommonResponse<PageResponse<UserGetAllResponseDto>> commonResponse = new CommonResponse<>(true, "사용자 전체 조회 성공", responseDto);

        return ResponseEntity.ok(commonResponse);
    }

    /**
     * 유저 정보(비밀번호) 수정 API
     */
    @PatchMapping("/{userId}")
    public ResponseEntity<CommonResponse<UserUpdateResponseDto>> updateUserApi(
            @AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestBody UserUpdateRequestDto requestDto
    ) {
        UserUpdateResponseDto responseDto = userService.updateUser(user, requestDto);

        CommonResponse<UserUpdateResponseDto> commonResponse = new CommonResponse<>(true, "회원 정보 수정 성공", responseDto);

        ResponseEntity<CommonResponse<UserUpdateResponseDto>> response = new ResponseEntity<>(commonResponse, HttpStatus.OK);

        return response;
    }

    /**
     * 유저 삭제(소프트 딜리트) API
     */
    @DeleteMapping
    public ResponseEntity deleteUserApi(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        userService.deleteUser(user);

        CommonResponse commonResponse = new CommonResponse<>(true, "회원 탈퇴 성공", null);

        ResponseEntity response = new ResponseEntity<>(commonResponse, HttpStatus.OK);

        return response;
    }

}
