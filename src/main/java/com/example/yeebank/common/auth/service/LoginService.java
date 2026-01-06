package com.example.yeebank.common.auth.service;

import com.example.yeebank.common.auth.dto.request.LoginRequestDto;
import com.example.yeebank.common.auth.dto.response.LoginResponseDto;
import com.example.yeebank.common.utils.JwtUtil;
import com.example.yeebank.domain.user.entity.User;
import com.example.yeebank.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDto login(LoginRequestDto request) {
        User user = userRepository.findUserByEmailAndIsDeletedFalse(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일 혹은 비밀번호가 일치하지 않습니다."));
        boolean matchPassword = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if(!matchPassword) {
            throw new IllegalArgumentException("이메일 혹은 비밀번호가 일치하지 않습니다.");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getName(), user.getEmail());

        return new LoginResponseDto(token);
    }
}
