package com.example.yeebank.domain.user.service;

import com.example.yeebank.domain.user.dto.request.UserPointEarnPointRequestDto;
import com.example.yeebank.domain.user.dto.request.UserPointUsePointRequestDto;
import com.example.yeebank.domain.user.dto.response.UserPointAttendancePointResponseDto;
import com.example.yeebank.domain.user.dto.response.UserPointEarnPointResponseDto;
import com.example.yeebank.domain.user.dto.response.UserPointGetMyPointResponseDto;
import com.example.yeebank.domain.user.dto.response.UserPointUsePointResponseDto;
import com.example.yeebank.domain.user.entity.User;
import com.example.yeebank.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class MyPointService {

    private final UserRepository userRepository;

    /**
     * 포인트 적립 로직
     */
    @Transactional
    public UserPointEarnPointResponseDto earnPoint(Long userId, UserPointEarnPointRequestDto requestDto) {

        User findUser = userRepository.findUserByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new RuntimeException(""));

        findUser.earnPoint(requestDto.getEarnPoint());

        UserPointEarnPointResponseDto responseDto = new UserPointEarnPointResponseDto(
                findUser.getId(),
                findUser.getName(),
                requestDto.getEarnPoint(),
                findUser.getMyPoint()
        );

        return responseDto;
    }

    /**
     * 출석체크 포인트 적립 로직
     */
    @Transactional
    public UserPointAttendancePointResponseDto attendancePoint(Long userId, LocalDate attendanceDate) {

        long attendancePoint = 100;

        LocalDate today = LocalDate.now();

        User findUser = userRepository.findUserByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new RuntimeException(""));

        Boolean validDate = today.equals(attendanceDate);

        if (!validDate) {
            throw new RuntimeException("출석체크는 오늘만 가능합니다");
        }

        findUser.earnAttendancePoint(today);

        UserPointAttendancePointResponseDto responseDto = new UserPointAttendancePointResponseDto(
                findUser.getId(),
                findUser.getName(),
                attendancePoint,
                findUser.getMyPoint()
        );

        return responseDto;
    }

    /**
     * 포인트 사용 로직
     */
    @Transactional
    public UserPointUsePointResponseDto usePoint(Long userId, UserPointUsePointRequestDto requestDto) {

        User findUser = userRepository.findUserByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new RuntimeException(""));

        findUser.usePoint(requestDto.getUsePoint());

        UserPointUsePointResponseDto responseDto = new UserPointUsePointResponseDto(
                findUser.getId(),
                findUser.getName(),
                requestDto.getUsePoint(),
                findUser.getMyPoint()
        );

        return responseDto;
    }

    /**
     * 포인트 조회 로직
     */
    @Transactional(readOnly = true)
    public UserPointGetMyPointResponseDto getMyPoint(Long userId) {

        User findUser = userRepository.findUserByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new RuntimeException(""));

        UserPointGetMyPointResponseDto responseDto = new UserPointGetMyPointResponseDto(
                findUser.getId(),
                findUser.getName(),
                findUser.getMyPoint()
        );

        return responseDto;
    }

}