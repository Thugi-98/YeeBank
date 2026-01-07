package com.example.yeebank.domain.user.repository;

import com.example.yeebank.domain.user.dto.dto.UserDto;
import com.example.yeebank.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByIdAndIsDeletedFalse(Long userId);

    Page<UserDto> findAllByIsDeletedFalse(Pageable pageable);

    Boolean existsByEmail(String email);
}
