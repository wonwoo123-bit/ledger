package com.example.ledger.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Entity @Table(name="issue_comments",
  indexes = @Index(name="idx_ic_issue_time", columnList="issue_id, created_at ASC"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class IssueComment {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="issue_id", nullable=false)
  private Issue issue;

  @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="user_id", nullable=false)
  private User user;

  @Lob @Column(nullable=false)
  private String body;

  @Column(name="created_at", insertable=false, updatable=false)
  private LocalDateTime createdAt;
}
