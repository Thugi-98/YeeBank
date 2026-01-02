package com.example.yeebank.domain.transfer.controller;

import com.example.yeebank.domain.transfer.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

}
