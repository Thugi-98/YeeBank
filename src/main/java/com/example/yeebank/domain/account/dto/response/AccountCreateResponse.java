package com.example.yeebank.domain.account.dto.response;

import com.example.yeebank.domain.account.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AccountCreateResponse {
    private final Long id;
    private final String accountNumber;
    private final String alias;
    private final Long balance;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public static AccountCreateResponse from(Account account) {
        return new AccountCreateResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getAlias(),
                account.getBalance(),
                account.getCreatedAt(),
                account.getModifiedAt()
        );

    }
}

