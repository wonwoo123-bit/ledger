package com.example.ledger.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class Auditable {
    @Column(name = "created_at", insertable = false, updatable = false)
    protected java.sql.Timestamp createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    protected java.sql.Timestamp updatedAt;
}
