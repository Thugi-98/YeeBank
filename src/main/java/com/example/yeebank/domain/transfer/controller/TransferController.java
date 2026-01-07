package com.example.yeebank.domain.transfer.controller;

import com.example.yeebank.common.dto.CommonResponse;
import com.example.yeebank.common.dto.PageResponse;
import com.example.yeebank.domain.transfer.dto.dto.TransferDto;
import com.example.yeebank.domain.transfer.dto.request.TransferDepositRequestDto;
import com.example.yeebank.domain.transfer.dto.request.TransferRemittanceRequestDto;
import com.example.yeebank.domain.transfer.dto.request.TransferWithdrawalRequestDto;
import com.example.yeebank.domain.transfer.dto.response.TransferDepositResponseDto;
import com.example.yeebank.domain.transfer.dto.response.TransferGetAllResponseDto;
import com.example.yeebank.domain.transfer.dto.response.TransferRemittanceResponseDto;
import com.example.yeebank.domain.transfer.dto.response.TransferWithdrawalResponseDto;
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
    @PostMapping("/deposit")
    public ResponseEntity<CommonResponse<TransferDepositResponseDto>> Doposit(
            @RequestBody TransferDepositRequestDto requestDto) {
        // - Get Result By Service
        TransferDto result = transferService.Deposit(
                requestDto.getToAccountId(), requestDto.getAmount());

        // - Return Result
        return ResponseEntity.ok(
                CommonResponse.success(
                        TransferDepositResponseDto.from(result), "입금 성공"));
    }
    @PostMapping("/withdrawal")
    public ResponseEntity<CommonResponse<TransferWithdrawalResponseDto>> Doposit(
            @RequestBody TransferWithdrawalRequestDto requestDto) {
        // - Get Result By Service
        TransferDto result = transferService.Withdrawal(
                requestDto.getFromAccountId(), requestDto.getAmount());

        // - Return Result
        return ResponseEntity.ok(
                CommonResponse.success(
                        TransferWithdrawalResponseDto.from(result), "출금 성공"));
    }
    @PostMapping("/remittance")
    public ResponseEntity<CommonResponse<TransferRemittanceResponseDto>> Doposit(
            @RequestBody TransferRemittanceRequestDto requestDto) {
        // - Get Result By Service
        TransferDto result = transferService.Remittance(
                requestDto.getFromAccountId(), requestDto.getToAccountId(), requestDto.getAmount());

        // - Return Result
        return ResponseEntity.ok(
                CommonResponse.success(
                        TransferRemittanceResponseDto.from(result), "송금 성공"));
    }
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
