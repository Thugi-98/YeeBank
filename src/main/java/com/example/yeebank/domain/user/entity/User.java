package com.example.yeebank.domain.user.entity;

import com.example.yeebank.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

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
