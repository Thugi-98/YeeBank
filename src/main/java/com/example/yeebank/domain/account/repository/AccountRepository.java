package com.example.yeebank.domain.account.repository;

import com.example.yeebank.domain.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    // 계좌 별칭 중복 체크(소프트 딜리트 제외)
    boolean existsByUserIdAndAliasAndIsDeletedFalse(Long userId, String alias);

    // 상세 조회에서 사용하는 메서드(소프트 딜리트 제외)
    Optional<Account> findByIdAndIsDeletedFalse(Long accountId);

    // 목록 조회에서 사용하는 메서드(소프트 딜리트 제외)
    List<Account> findAllByUserIdAndIsDeletedFalse(Long userId);
}
