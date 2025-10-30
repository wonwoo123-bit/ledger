package com.example.ledger.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ledger.domain.Document;
import com.example.ledger.enums.DocumentStatus;
import com.example.ledger.enums.DocumentType;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    Page<Document> findByUserIdAndPayDateBetween(Long userId, LocalDate from, LocalDate to, Pageable pageable);

    Page<Document> findByUserIdAndStatus(Long userId, DocumentStatus status, Pageable pageable);

    Page<Document> findByUserIdAndType(Long userId, DocumentType type, Pageable pageable);
}
