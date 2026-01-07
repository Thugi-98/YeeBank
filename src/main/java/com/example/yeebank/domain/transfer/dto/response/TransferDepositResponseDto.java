package com.example.yeebank.domain.transfer.dto.response;

import com.example.yeebank.domain.transfer.dto.dto.TransferDto;
import com.example.yeebank.domain.transfer.enums.TransferStatus;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class TransferDepositResponseDto {
// - Properties
    private final Long id;
    private final Long toAccountId;
    private final Long amount;
    private final TransferStatus status;
    private final String failReason;
    private final Long requestClientId;
    private final LocalDateTime createAt;

// - Methods
    static public TransferDepositResponseDto from(TransferDto dto) {
        return new TransferDepositResponseDto(
                dto.getId(),
                dto.getToAccountId(),
                dto.getAmount(),
                dto.getStatus(),
                dto.getFailReason(),
                dto.getRequestClientId(),
                dto.getCreateAt()
        );
    }
}
