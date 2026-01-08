package com.example.yeebank.domain.transfer.controller;

import com.example.yeebank.common.dto.CommonResponse;
import com.example.yeebank.common.dto.PageResponse;
import com.example.yeebank.domain.transfer.dto.response.TransferGetAllResponseDto;
import com.example.yeebank.domain.transfer.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transfer")
@RequiredArgsConstructor
public class TransferController {
// - Properties
    private final TransferService transferService;

// - Methods
    @GetMapping
    public ResponseEntity<CommonResponse<PageResponse<TransferGetAllResponseDto>>> GetAll(
            @RequestParam(required = false , defaultValue = "0") int page,
            @RequestParam(required = false , defaultValue = "10") int size) {
        // - Set Pageable
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        // - Get Result
        Page<TransferGetAllResponseDto> result = transferService.GetAll(pageable)
                .map(TransferGetAllResponseDto::from);

        // - Return Result
        return ResponseEntity.ok(
                CommonResponse.success(
                        PageResponse.from(result), "전체 조회 성공"));
    }
}
