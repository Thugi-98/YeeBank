package com.example.yeebank.domain.user.controller;

import com.example.yeebank.domain.user.dto.response.UserPointAttendancePointResponseDto;
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

    @PostMapping("/{userId}")
    public ResponseEntity<UserPointAttendancePointResponseDto> attendancePointApi(
            @PathVariable("userId") Long userId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate attendanceDate
    ) {
        UserPointAttendancePointResponseDto responseDto = myPointService.attendancePoint(userId, attendanceDate);

        ResponseEntity<UserPointAttendancePointResponseDto> response = new ResponseEntity<>(responseDto, HttpStatus.OK);

        return response;
    }
}