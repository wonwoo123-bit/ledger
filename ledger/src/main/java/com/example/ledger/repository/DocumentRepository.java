package com.example.ledger.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ledger.domain.Document;

public interface DocumentRepository extends JpaRepository<Document, Long>{
    Page<Document> findByUserId(Long userId, Pageable pageabel);
    Page<Document> findByUserIdAndDateBetween(Long UserId, LocalDate start, LocalDate end, Pageable pageable);
    Optional<Document> findByIdAndUserId(Long id, Long userId);

}
