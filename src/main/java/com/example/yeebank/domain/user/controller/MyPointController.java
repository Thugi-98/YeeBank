package com.example.yeebank.domain.user.controller;

import com.example.yeebank.common.dto.CommonResponse;
import com.example.yeebank.domain.user.dto.request.UserPointEarnPointRequestDto;
import com.example.yeebank.domain.user.dto.request.UserPointUsePointRequestDto;
import com.example.yeebank.domain.user.dto.response.UserPointAttendancePointResponseDto;
import com.example.yeebank.domain.user.dto.response.UserPointEarnPointResponseDto;
import com.example.yeebank.domain.user.dto.response.UserPointGetMyPointResponseDto;
import com.example.yeebank.domain.user.dto.response.UserPointUsePointResponseDto;
import com.example.yeebank.domain.user.service.MyPointService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<CommonResponse<UserPointEarnPointResponseDto>> earnPointApi(
            @PathVariable("userId") Long userId,
            @RequestBody UserPointEarnPointRequestDto requestDto
    ) {
        UserPointEarnPointResponseDto responseDto = myPointService.earnPoint(userId, requestDto);

        CommonResponse<UserPointEarnPointResponseDto> commonResponse = new CommonResponse<>(true, "포인트 적립 완료", responseDto);

        return new ResponseEntity<>(commonResponse, HttpStatus.OK);
    }

    /**
     * 출석체크 포인트 적립 API
     */
    @PostMapping("/{userId}/attendance")
    public ResponseEntity<CommonResponse<UserPointAttendancePointResponseDto>> attendancePointApi(
            @PathVariable("userId") Long userId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate attendanceDate
    ) {
        UserPointAttendancePointResponseDto responseDto = myPointService.attendancePoint(userId, attendanceDate);

        CommonResponse<UserPointAttendancePointResponseDto> commonResponse = new CommonResponse<>(true, "출석체크 완료", responseDto);

        return new ResponseEntity<>(commonResponse, HttpStatus.OK);
    }

    /**
     * 포인트 사용 API
     */
    @PutMapping("/{userId}")
    public ResponseEntity<CommonResponse<UserPointUsePointResponseDto>> usePointApi(
            @PathVariable("userId") Long userId,
            @RequestBody UserPointUsePointRequestDto requestDto
    ) {
        UserPointUsePointResponseDto responseDto = myPointService.usePoint(userId, requestDto);

        CommonResponse<UserPointUsePointResponseDto> commonResponse = new CommonResponse<>(true, "포인트 사용 완료", responseDto);

        return new ResponseEntity<>(commonResponse, HttpStatus.OK);
    }

    /**
     * 포인트 조회 API
     */
    @GetMapping("/{userId}")
    public ResponseEntity<CommonResponse<UserPointGetMyPointResponseDto>
            > getPointApi(
            @PathVariable("userId") Long userId
    ) {
        UserPointGetMyPointResponseDto responseDto = myPointService.getMyPoint(userId);

        CommonResponse<UserPointGetMyPointResponseDto> commonResponse = new CommonResponse<>(true, "포인트 조회 완료", responseDto);

        return new ResponseEntity<>(commonResponse, HttpStatus.OK);
    }
}