package com.example.ledger.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ledger.domain.Document;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    Page<Document> findByUserIdAndPayDateBetweenOrderByPayDateDescIdDesc(Long userId,
            LocalDate from, LocalDate to, Pageable pageable);
}
