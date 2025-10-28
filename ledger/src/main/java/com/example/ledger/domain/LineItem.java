package com.example.ledger.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Table(name="line_items",
  indexes = {
    @Index(name="idx_items_doc", columnList="document_id"),
    @Index(name="idx_items_cat", columnList="category_id")
  })
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LineItem {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="document_id", nullable=false)
  private Document document;

  @Column(length=200)
  private String name;

  @Column(precision=12, scale=2)
  private BigDecimal quantity = BigDecimal.valueOf(1);

  @Column(name="unit_price", precision=12, scale=2)
  private BigDecimal unitPrice;

  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="category_id")
  private Category category;

  @Column(name="created_at", insertable=false, updatable=false)
  private LocalDateTime createdAt;
}
