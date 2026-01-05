CREATE TABLE accounts
(
    id             BIGINT(20)         NOT NULL AUTO_INCREMENT COMMENT '계좌 식별자',
    user_id        BIGINT(20)         NOT NULL COMMENT '회원 식별자(FK)',
    account_number VARCHAR(32) UNIQUE NOT NULL COMMENT '계좌 번호',
    password       BIGINT(20)         NOT NULL COMMENT '계좌 비밀번호',
    alias          VARCHAR(50) UNIQUE NOT NULL COMMENT '별칭',
    balance        BIGINT             NOT NULL COMMENT '잔액',
    is_deleted     TINYINT(1)         NOT NULL DEFAULT 0 COMMENT '0: 삭제안됨, 1: 삭제됨',
    created_at     DATETIME           NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    updated_at     DATETIME           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    deleted_at     DATETIME           NULL DEFAULT NULL COMMENT '비활성일',
    PRIMARY KEY (id),
    CONSTRAINT fk_accounts_users
        FOREIGN KEY (user_id)
            REFERENCES users(id)
            ON UPDATE RESTRICT
            ON DELETE RESTRICT
) CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;