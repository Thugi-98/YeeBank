package com.example.yeebank.common.auth.controller;

import com.example.yeebank.common.auth.dto.request.LoginRequestDto;
import com.example.yeebank.common.auth.dto.response.LoginResponseDto;
import com.example.yeebank.common.auth.service.LoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/login")
public class LoginController {

    private final LoginService loginService;

    @PostMapping
    public ResponseEntity<LoginResponseDto> loginApi(@Valid @RequestBody LoginRequestDto request) {

        String token = loginService.login(request);

        return ResponseEntity.ok(new LoginResponseDto(token));
    }
}
