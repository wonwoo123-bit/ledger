package com.example.ledger.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ledger.domain.LineItem;

public interface LineItemRepository extends JpaRepository<LineItem, Long> {
}
