package com.example.yeebank.domain.user.entity;

import com.example.yeebank.common.entity.BaseEntity;
import com.example.yeebank.common.exception.CustomException;
import com.example.yeebank.common.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    private LocalDate attendanceAt = LocalDate.now().minusDays(1);

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void updateUser(String password) {
        this.password = password;
    }

    // 포인트
    public void earnPoint(long earn) {
        this.myPoint += earn;
    }

    public void earnAttendancePoint(LocalDate today) {

        if (attendanceAt.equals(today)) {
            throw new CustomException(ErrorCode.POINT_DUPLICATE_ATTENDANCE);
        }

        this.attendanceAt = today;
        earnPoint(100);
    }

    public void usePoint(long usePoint) {

        if (usePoint < 1) {
            throw new CustomException(ErrorCode.POINT_USE_BELOW_MINIUM);
        }

        if (this.myPoint < usePoint) {
            throw new CustomException(ErrorCode.POINT_USE_NOT_ENOUGH);
        }

        this.myPoint -= usePoint;
    }
}
