package com.example.yeebank.common.auth.service;

import com.example.yeebank.common.auth.dto.request.LoginRequestDto;
import com.example.yeebank.common.security.JwtUtil;
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

    public String login(LoginRequestDto request) {

        String email = request.getEmail();
        String password = request.getPassword();

        User user = userRepository.findUserByEmailAndIsDeletedFalse(email)
                .orElseThrow(() -> new IllegalArgumentException("이메일 혹은 비밀번호가 일치하지 않습니다."));

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("이메일 혹은 비밀번호가 일치하지 않습니다.");
        }

        return jwtUtil.generateToken(user.getName(), user.getEmail());
    }
}
