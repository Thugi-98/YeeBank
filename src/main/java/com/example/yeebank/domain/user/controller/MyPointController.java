package com.example.yeebank.domain.user.controller;

import com.example.yeebank.domain.user.dto.request.UserPointEarnPointRequestDto;
import com.example.yeebank.domain.user.dto.request.UserPointUsePointRequestDto;
import com.example.yeebank.domain.user.dto.response.UserPointAttendancePointResponseDto;
import com.example.yeebank.domain.user.dto.response.UserPointEarnPointResponseDto;
import com.example.yeebank.domain.user.dto.response.UserPointUsePointResponseDto;
import com.example.yeebank.domain.user.service.MyPointService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/users/point")
@RequiredArgsConstructor
public class MyPointController {

    private final MyPointService myPointService;

    /**
     * 포인트 적립 API
     */
    @PostMapping("/{userId}")
    public ResponseEntity<UserPointEarnPointResponseDto> earnPointApi(
            @PathVariable("userId") Long userId,
            @RequestBody UserPointEarnPointRequestDto requestDto
    ) {
        UserPointEarnPointResponseDto responseDto = myPointService.earnPoint(userId, requestDto);

        ResponseEntity<UserPointEarnPointResponseDto> response = new ResponseEntity<>(responseDto, HttpStatus.OK);

        return response;
    }

    /**
     * 출석체크 포인트 적립 API
     */
    @PostMapping("/{userId}/attendance")
    public ResponseEntity<UserPointAttendancePointResponseDto> attendancePointApi(
            @PathVariable("userId") Long userId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate attendanceDate
    ) {
        UserPointAttendancePointResponseDto responseDto = myPointService.attendancePoint(userId, attendanceDate);

        ResponseEntity<UserPointAttendancePointResponseDto> response = new ResponseEntity<>(responseDto, HttpStatus.OK);

        return response;
    }

    /**
     * 포인트 사용 API
     */
    @PutMapping("/{userId}")
    public ResponseEntity<UserPointUsePointResponseDto> usePoint(
            @PathVariable("userId") Long userId,
            @RequestBody UserPointUsePointRequestDto requestDto
            ) {
        UserPointUsePointResponseDto responseDto = myPointService.usePoint(userId, requestDto);

        ResponseEntity<UserPointUsePointResponseDto> response = new ResponseEntity<>(responseDto, HttpStatus.OK);

        return response;
    }
}