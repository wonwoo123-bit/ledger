package com.example.ledger.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ledger.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{
    boolean existsByName(String name);

}
