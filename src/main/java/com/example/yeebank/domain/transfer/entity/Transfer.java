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

    @Column(nullable = false)
    private Long amount;

    @Column(length = 500, nullable = false)
    private String status;

    @Column(length = 200)
    private String fail_reason;

    @Column
    private Long request_client_id;
}
