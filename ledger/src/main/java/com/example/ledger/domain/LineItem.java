package com.example.ledger.domain;

import com.example.ledger.domain.Category;
import com.example.ledger.domain.Auditable;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "line_items", indexes = {
    @Index(name = "idx_items_doc", columnList = "document_id"),
    @Index(name = "idx_items_cat", columnList = "category_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LineItem extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "document_id", foreignKey = @ForeignKey(name = "fk_items_doc"))
  private Document document;

  @Column(length = 200)
  private String name;

  @Column(precision = 12, scale = 2)
  private BigDecimal quantity = BigDecimal.valueOf(1);

  @Column(name = "unit_price", precision = 12, scale = 2)
  private BigDecimal unitPrice;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "fk_items_cat"))
  private Category category;
}
