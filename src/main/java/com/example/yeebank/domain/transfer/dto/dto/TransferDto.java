package com.example.yeebank.domain.transfer.dto.dto;

import com.example.yeebank.domain.transfer.entity.Transfer;
import com.example.yeebank.domain.transfer.enums.TransferStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TransferDto {
// - Properties
    private Long id;
    private Long fromAccountId;
    private Long toAccountId;
    private Long amount;
    private TransferStatus status;
    private String failReason;
    private Long requestClientId;
    private LocalDateTime createAt;

// - Methods
    // - Static Factory Methods
    public static TransferDto from(Transfer transfer) {
        return new TransferDto(
                transfer.getId(),
                transfer.getFromAccountId(),
                transfer.getToAccountId(),
                transfer.getAmount(),
                transfer.getStatus(),
                transfer.getFailReason(),
                transfer.getRequestClientId(),
                transfer.getCreatedAt()
        );
    }
}
