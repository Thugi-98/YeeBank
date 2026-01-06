package com.example.yeebank.domain.transfer.entity;

import com.example.yeebank.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "transfers")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transfer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 임의로 String으로 지정했습니다! 편한 타입으로 변경하셔도 됩니다
    @Column(length = 32, nullable = false)
    private String account_number;

    @Column(nullable = false)
    private Long password;

    @Column(length = 50, nullable = false)
    private String alias;

    @Column(nullable = false)
    private Long balance;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @Column
    private LocalDateTime deletedAt;

}
