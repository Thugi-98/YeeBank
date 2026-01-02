package com.example.yeebank.domain.transfer.service;

import com.example.yeebank.domain.transfer.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransferService {

    private final TransferRepository transferRepository;

}
