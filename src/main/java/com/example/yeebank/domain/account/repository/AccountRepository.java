package com.example.yeebank.domain.account.repository;

import com.example.yeebank.domain.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

    // 계좌 별칭 중복 체크(소프트 딜리트 제외)
    boolean existsByUserIdAndAliasAndIsDeletedFalse(Long userId, String alias);
}
