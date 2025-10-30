package com.example.ledger.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ledger.domain.Budget;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Optional<Budget> findByUserIdAndMonthYmAndCategoryId(Long userId, String monthYm, Long categoryId);
}
