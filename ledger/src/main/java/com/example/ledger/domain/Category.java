package com.example.ledger.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

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
