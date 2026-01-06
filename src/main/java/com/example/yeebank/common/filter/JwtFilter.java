package com.example.yeebank.common.filter;

import com.example.yeebank.common.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        // 토큰을 발급받는 로그인의 경우에는 토큰 검사를 하지 않아도 통과
        String requestMethod = request.getMethod();
        String requestURL = request.getRequestURI();

        if(requestURL.equals("/api/users") && requestMethod.equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        if(requestURL.equals("/api/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 너 토큰 있어? 없어?
        String authorizationHeader = request.getHeader("Authorization");

        if(authorizationHeader == null || authorizationHeader.isBlank()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT 토큰이 필요합니다.");
            return;
        }

        // 토큰이 있어? 그럼 그 토큰 유효해?

        String jwt = authorizationHeader.substring(7);

        if(!jwtUtil.validateToken(jwt)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"error\": \"Unauthorized\"}");
        }

        // 유효하면 어떤 정보를 가지고 있어?

        String email = jwtUtil.extractEmail(jwt);

        // request.setAttribute("email", email);

        // Spring Security에서 사용하는 User 객체 생성
        User user = new User(email, "", List.of());

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));

        filterChain.doFilter(request, response);

    }
}
