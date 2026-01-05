package com.example.yeebank.common.auth.controller;

import com.example.yeebank.common.auth.dto.request.LoginRequest;
import com.example.yeebank.common.auth.dto.response.LoginResponse;
import com.example.yeebank.common.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/login")
public class AuthController {

    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        String token = jwtUtil.generateToken(request.getEmail());

        return ResponseEntity.ok(new LoginResponse(token));
    }
}
