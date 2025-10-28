package com.example.ledger.domain;

import java.time.LocalDateTime;

import com.example.ledger.enums.IssueSeverity;
import com.example.ledger.enums.IssueStatus;

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

@Entity
@Table(name = "issues", indexes = {
        @Index(name = "idx_issues_status", columnList = "status, severity, created_at DESC"),
        @Index(name = "idx_issues_reporter", columnList = "reporter_user_id, created_at DESC")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_user_id", nullable = false)
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_user_id")
    private User assignee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private IssueSeverity severity = IssueSeverity.LOW;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private IssueStatus status = IssueStatus.OPEN;

    @Column(nullable = false, length = 200)
    private String title;

    @Lob
    private String description;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;
}
