package com.example.yeebank.domain.account.dto.response;

import com.example.yeebank.domain.account.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AccountUpdateResponse {
    private final Long id;
    private final String accountNumber;
    private final String alias;
    private final LocalDateTime updatedAt;

    public static AccountUpdateResponse from(Account account) {
        return new AccountUpdateResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getAlias(),
                account.getUpdatedAt()

        );
    }
}
