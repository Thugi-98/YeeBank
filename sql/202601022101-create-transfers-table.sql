CREATE TABLE transfers
(
    id                BIGINT(20)                NOT NULL AUTO_INCREMENT COMMENT '거래내역 식별자',
    from_account_id   BIGINT(20)                NOT NULL COMMENT '보내는 게좌(FK)',
    to_account_id     BIGINT(20)                NOT NULL COMMENT '받는 계좌(FK)',
    amount            VARCHAR(20)               NOT NULL COMMENT '금액',
    status            ENUM ('SUCCESS','FAILED') NOT NULL COMMENT '결과 상태',
    fail_reason       VARCHAR(200)              NULL COMMENT '실패 사유',
    request_client_id BIGINT                    NULL COMMENT '요청 클라이언트 id',
    created_at        DATETIME                  NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    PRIMARY KEY (id),
    CONSTRAINT fk_transfers_from_account
        FOREIGN KEY (from_account_id)
            REFERENCES accounts (id)
            ON UPDATE RESTRICT
            ON DELETE RESTRICT,
    CONSTRAINT fk_transfers_to_account
        FOREIGN KEY (to_account_id)
            REFERENCES accounts (id)
            ON UPDATE RESTRICT
            ON DELETE RESTRICT
) CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;