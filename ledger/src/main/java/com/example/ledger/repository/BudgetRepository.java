package com.example.ledger.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ledger.domain.Budget;

public interface BudgetRepository extends JpaRepository<Budget, Long>{
    boolean existsByUserIdAndMonthYmAndCategoryId(Long userId, String monthYm, Long categoryId);

}
