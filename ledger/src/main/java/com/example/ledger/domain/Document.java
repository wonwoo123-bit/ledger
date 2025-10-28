package com.example.ledger.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.ledger.enums.DocumentStatus;
import com.example.ledger.enums.DocumentType;
import com.example.ledger.enums.PaymentMethod;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Table(name="documents",
  indexes = {
    @Index(name="idx_docs_user_date", columnList="user_id, pay_date DESC, id DESC"),
    @Index(name="idx_docs_type_date", columnList="type, pay_date, id"),
    @Index(name="idx_docs_store", columnList="store_name"),
    @Index(name="idx_docs_status", columnList="status"),
    @Index(name="idx_docs_created", columnList="created_at")
  })
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Document {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="user_id", nullable=false)
  private User user;

  @Enumerated(EnumType.STRING)
  @Column(nullable=false, length=10)
  private DocumentType type = DocumentType.EXPENSE;

  @Column(length=200)
  private String title;

  @Column(name="store_name", length=200)
  private String storeName;

  @Column(name="pay_date")
  private LocalDate payDate;

  @Column(name="total_amount")
  private Long totalAmount;

  @Enumerated(EnumType.STRING)
  @Column(name="payment_method", length=10)
  private PaymentMethod paymentMethod = PaymentMethod.UNKNOWN;

  @Lob @Column(name="raw_text")
  private String rawText;

  @Column(name="file_url", nullable=false, length=1000)
  private String fileUrl;

  @Enumerated(EnumType.STRING)
  @Column(nullable=false, length=10)
  private DocumentStatus status = DocumentStatus.IMPORTED;

  @Column(name="created_at", insertable=false, updatable=false)
  private LocalDateTime createdAt;

  @Column(name="updated_at", insertable=false, updatable=false)
  private LocalDateTime updatedAt;
}
