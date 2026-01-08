package com.example.yeebank.domain.account.dto.response;

import com.example.yeebank.common.aop.annotation.RedisLock;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AccountTransferResponse {
// - Properties
    private final Long id;
    private final Long balance;
}
