package com.example.ledger.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ledger.domain.User;
import com.example.ledger.enums.UserRole;
import com.example.ledger.enums.UserStatus;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByStatus(UserStatus status);

    Optional<User> findByRole(UserRole role);

    boolean existsByEmail(String email);
}
