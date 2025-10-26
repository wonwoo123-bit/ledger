package com.example.ledger.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ledger.domain.User;
import com.example.ledger.domain.User.Status;

public interface UserRepository extends JpaRepository<User, Long>{
       /** 이메일로 사용자 조회 (로그인 등) */
    Optional<User> findByEmail(String email);

    /** 이메일 중복 체크 */
    boolean existsByEmail(String email);

    /** 상태 포함 단건 조회 (활성 사용자만 조회하고 싶을 때) */
    Optional<User> findByIdAndStatus(Long id, Status status);

    /** 상태별 목록 페이징 조회 */
    Page<User> findByStatus(Status status, Pageable pageable);

}
