package com.example.yeebank.domain.transfer.entity;

import com.example.yeebank.domain.transfer.enums.TransferStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "transfers")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Transfer {
// - Properties
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "from_account_id")
    private Long fromAccountId;
    @Column(name = "to_account_id")
    private Long toAccountId;
    @Column(name = "amount", nullable = false)
    private Long amount;
    @Column(name = "status", nullable = false) @Enumerated(EnumType.STRING)
    private TransferStatus status;
    @Column(name = "fail_reason", length = 200)
    private String failReason;
    @Column(name = "created_at") @CreatedDate
    private LocalDateTime createdAt;

// - Methods
    public Transfer(Long fromAccountId, Long toAccountId, Long amount, TransferStatus status, String failReason) {
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.status = status;
        this.failReason = failReason;
    }
}
