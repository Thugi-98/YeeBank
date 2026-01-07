package com.example.yeebank.domain.transfer.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class TransferDepositRequestDto {
// - Properties
    @NotNull(message = "입금 계좌를 입력해주세요.")
    private Long toAccountId;
    @NotNull(message = "입금액을 입력해주세요.") @Positive(message = "금액은 양수만 가능합니다.")
    private Long amount;
}
