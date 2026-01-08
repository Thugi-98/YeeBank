package com.example.yeebank.domain.transfer.dto.response;

import com.example.yeebank.domain.transfer.dto.dto.TransferDto;
import com.example.yeebank.domain.transfer.enums.TransferStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class TransferGetAllResponseDto {
// - Properties
    private final Long id;
    private final Long fromAccountId;
    private final Long toAccountId;
    private final Long amount;
    private final TransferStatus status;
    private final String failReason;
    private final LocalDateTime createdAt;

// - Methods
    static public TransferGetAllResponseDto from(TransferDto dto) {
        return new TransferGetAllResponseDto(
                dto.getId(),
                dto.getFromAccountId(),
                dto.getToAccountId(),
                dto.getAmount(),
                dto.getStatus(),
                dto.getFailReason(),
                dto.getCreatedAt()
        );
    }
}
