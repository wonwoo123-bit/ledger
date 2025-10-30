package com.example.ledger.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ledger.domain.Issue;
import com.example.ledger.enums.IssueStatus;

public interface IssueRepository extends JpaRepository<Issue, Long> {
    List<Issue> findTop20ByStatusOrderByCreatedAtDesc(IssueStatus status);
}
