package com.example.ledger.domain;

import java.sql.Timestamp;

import com.example.ledger.enums.IssueSeverity;
import com.example.ledger.enums.IssueStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
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
public class Issue extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_user_id", foreignKey = @ForeignKey(name = "fk_issues_reporter"))
    private User reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_user_id", foreignKey = @ForeignKey(name = "fk_issues_assignee"))
    private User assignee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 8)
    private IssueSeverity severity = IssueSeverity.LOW;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 12)
    private IssueStatus status = IssueStatus.OPEN;

    @Column(nullable = false, length = 200)
    private String title;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "resolved_at")
    private Timestamp resolvedAt;
}
