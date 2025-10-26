package com.example.ledger.domain;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "documents")
@Getter
@Setter
@NoArgsConstructor
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // FK: user_id
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 7)
    private Type type = Type.EXPENSE;

    private String title;
    private String storeName;
    private LocalDate payDate;

    /** 원화 정수(원) */
    private Long totalAmount;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod = PaymentMethod.UNKNOWN;

    @Column(length = 1000, nullable = false) // 업로드 파일 경로
    private String fileUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.IMPORTED;

    @Version // 낙관적 락(동시 수정 충돌 방지)
    private Long version;

    public enum Type {
        EXPENSE, INCOME
    }

    public enum PaymentMethod {
        CARD, CASH, UNKNOWN
    }

    public enum Status {
        IMPORTED, CONFIRMED
    }
}
