package com.example.yeebank.domain.account.entity;

import com.example.yeebank.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@Table(name = "accounts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(length = 32, nullable = false, unique = true)
    private String accountNumber;

    @Column(length = 50, nullable = false)
    private String alias;

    @Column(nullable = false)
    private Long balance;

//    @Column(length = 50, nullable = false)
//    private String name;
//
//    @Column(length = 320, unique = true, nullable = false)
//    private String email;

    @Column(nullable = false)
    private Long password;

//    @Column(nullable = false)
//    private Boolean isDeleted;

    @Builder
    public Account(Long userId,
                   String accountNumber,
                   Long password,
                   String alias,
                   Long balance) {

        this.userId = userId;
        this.accountNumber = accountNumber;
        this.password = password;
        this.alias = alias;
        this.balance = balance;

    }
    // 별칭 업데이트 메서드
    public void updateAlias(@NotBlank(message = "계좌 별칭은 필수입니다") @Size(max = 50, message = "별칭은 50자 이하여야 합니다") String alias) {
        this.alias = alias;
    }
}

//    @Column
//    private Boolean isDeleted = false;
//
//    @Column
//    private LocalDateTime deletedAt;

