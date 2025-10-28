package com.example.ledger.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Table(name="categories",
  indexes = @Index(name="idx_categories_name", columnList="name", unique = true))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Category {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable=false, length=100, unique=true)
  private String name;

  @Column(name="created_at", insertable=false, updatable=false)
  private LocalDateTime createdAt;
}
