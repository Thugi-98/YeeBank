package com.example.yeebank.domain.transfer.controller;

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
    public ResponseEntity<TransferDepositResponseDto> Doposit(
            @RequestBody TransferDepositRequestDto requestDto) {
        // - Get Result By Service
        TransferDto result = transferService.Deposit(
                requestDto.getToAccountId(), requestDto.getAmount());

        // - Return Result
        return ResponseEntity.ok(TransferDepositResponseDto.from(result));
    }
    @PostMapping("/withdrawal")
    public ResponseEntity<TransferWithdrawalResponseDto> Doposit(
            @RequestBody TransferWithdrawalRequestDto requestDto) {
        // - Get Result By Service
        TransferDto result = transferService.Withdrawal(
                requestDto.getFromAccountId(), requestDto.getAmount());

        // - Return Result
        return ResponseEntity.ok(TransferWithdrawalResponseDto.from(result));
    }
    @PostMapping("/remittance")
    public ResponseEntity<TransferRemittanceResponseDto> Doposit(
            @RequestBody TransferRemittanceRequestDto requestDto) {
        // - Get Result By Service
        TransferDto result = transferService.Remittance(
                requestDto.getFromAccountId(), requestDto.getToAccountId(), requestDto.getAmount());

        // - Return Result
        return ResponseEntity.ok(TransferRemittanceResponseDto.from(result));
    }
    @GetMapping
    public ResponseEntity<Page<TransferGetAllResponseDto>> GetAll(
            @RequestParam(required = false , defaultValue = "0") int page,
            @RequestParam(required = false , defaultValue = "10") int size) {
        // - Set Pageable
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        // - Get Result
        Page<TransferDto> result = transferService.GetAll(pageable);

        // - Return Result
        return ResponseEntity.ok(result.map(TransferGetAllResponseDto::from));
    }
}
