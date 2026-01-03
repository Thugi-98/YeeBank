package com.example.yeebank.domain.user.entity;

import com.example.yeebank.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 320, unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private long myPoint = 0;

    @Column
    private Boolean isDeleted = false;

    @Column
    private LocalDateTime deletedAt;

    @Column
    private LocalDateTime attendanceAt;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void updateUser(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void deleteUser() {
        this.isDeleted = true;
    }

    // 포인트
    public void earnPoint(long earn) {
        this.myPoint += earn;
    }

    public void earnAttendancePoint(LocalDateTime today) {
        if (attendanceAt != null && attendanceAt.toLocalDate().equals(today.toLocalDate())) {
            throw new RuntimeException("오늘은 이미 출석체크 완료 되었습니다");
        }

        this.attendanceAt = today;
        earnPoint(100);
    }

    public void usePoint(long usePoint) {

        if (usePoint < 0) {
            throw new RuntimeException("사용가능 하신 포인트는 1 포인트 이상입니다");
        }

        if (this.myPoint < usePoint) {
            throw new RuntimeException("보유하신 포인트가 부족합니다");
        }

        this.myPoint -= usePoint;
    }
}
