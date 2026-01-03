package com.example.yeebank.domain.user.service;

import com.example.yeebank.domain.user.dto.response.UserPointAttendancePointResponseDto;
import com.example.yeebank.domain.user.entity.User;
import com.example.yeebank.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MyPointService {

    private final UserRepository userRepository;


    @Transactional
    public void earnPoint() {

    }

    @Transactional
    public UserPointAttendancePointResponseDto attendancePoint(Long userId ,LocalDate attendanceDate) {

        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();

        User findUser = userRepository.findUserByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new RuntimeException(""));

        Boolean validDate = today.equals(attendanceDate);

        if (!validDate) {
            throw new RuntimeException("출석체크는 오늘만 가능합니다");
        }

        findUser.earnAttendancePoint(now);

        UserPointAttendancePointResponseDto responseDto = new UserPointAttendancePointResponseDto(
                findUser.getId(),
                findUser.getName(),
                100,
                findUser.getMyPoint()
        );

        return responseDto;

    }

    @Transactional
    public void usePoint() {

    }

    @Transactional
    public void getMyPoint() {

    }

}