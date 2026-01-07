package com.example.yeebank.domain.user.repository;

import com.example.yeebank.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByIdAndIsDeletedFalse(Long userId);

    Optional<User> findUserByEmailAndIsDeletedFalse(String email);

    List<User> findUsersByIsDeletedFalse();

    Boolean existsByEmail(String email);
}
