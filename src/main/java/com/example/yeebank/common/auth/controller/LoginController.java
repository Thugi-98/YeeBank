package com.example.yeebank.common.auth.controller;

import com.example.yeebank.common.auth.dto.request.LoginRequestDto;
import com.example.yeebank.common.auth.dto.response.LoginResponseDto;
import com.example.yeebank.common.auth.service.LoginService;
import com.example.yeebank.common.dto.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/login")
public class LoginController {

    private final LoginService loginService;

    /**
     * 로그인 API
     */
    @PostMapping
    public ResponseEntity<CommonResponse<LoginResponseDto>> loginApi(@Valid @RequestBody LoginRequestDto request) {

        String token = loginService.login(request);

        LoginResponseDto responseDto = new LoginResponseDto(token);

        CommonResponse<LoginResponseDto> commonResponse = new CommonResponse<>(true, "로그인 성공", responseDto);

        ResponseEntity<CommonResponse<LoginResponseDto>> response = new ResponseEntity<>(commonResponse, HttpStatus.OK);

        return response;
    }
}
