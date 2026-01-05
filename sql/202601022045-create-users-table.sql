CREATE TABLE users
(
    id         BIGINT(20)          NOT NULL AUTO_INCREMENT COMMENT '회원 식별자',
    name       VARCHAR(50)         NOT NULL COMMENT '회원 이름',
    email      VARCHAR(320) UNIQUE NOT NULL COMMENT '회원 이메일',
    password   VARCHAR(255)        NOT NULL COMMENT '회원 비밀번호',
    myPoint    BIGINT              NOT NULL COMMENT '회원 포인트',
    is_deleted TINYINT(1)          NOT NULL DEFAULT 0 COMMENT '0: 삭제안됨, 1: 삭제됨',
    created_at DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    updated_at DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    deleted_at DATETIME            NULL DEFAULT NULL COMMENT '비활성일',
    attendanceAt DATETIME          NULL DEFAULT NULL COMMENT '출석체크일',
    PRIMARY KEY (id)
) CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;