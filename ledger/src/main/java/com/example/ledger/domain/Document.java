package com.example.ledger.domain;

import com.example.ledger.domain.Auditable;
import com.example.ledger.enums.DocumentStatus;
import com.example.ledger.enums.DocumentType;
import com.example.ledger.enums.PaymentMethod;
import com.example.ledger.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;

@Entity
@Table(name = "documents", indexes = {
    @Index(name = "idx_docs_user_date", columnList = "user_id, pay_date DESC, id DESC"),
    @Index(name = "idx_docs_type_date", columnList = "type, pay_date, id"),
    @Index(name = "idx_docs_store", columnList = "store_name"),
    @Index(name = "idx_docs_status", columnList = "status"),
    @Index(name = "idx_docs_created", columnList = "created_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_docs_user"))
  private User user;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 7)
  private DocumentType type = DocumentType.EXPENSE;

  @Column(length = 200)
  private String title;

  @Column(name = "store_name", length = 200)
  private String storeName;

  @Column(name = "pay_date")
  private LocalDate payDate;

  @Column(name = "total_amount")
  private Long totalAmount;

  @Enumerated(EnumType.STRING)
  @Column(name = "payment_method", length = 10)
  private PaymentMethod paymentMethod = PaymentMethod.UNKNOWN;

  /** MEDIUMTEXT 매핑(스키마 검증 불일치 방지) */
  @JdbcTypeCode(SqlTypes.LONGVARCHAR) // Hibernate 타입 힌트
  @Column(name = "raw_text", columnDefinition = "MEDIUMTEXT")
  private String rawText;

  @Column(name = "file_url", nullable = false, length = 1000)
  private String fileUrl;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 10)
  private DocumentStatus status = DocumentStatus.IMPORTED;
}
