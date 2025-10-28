package com.example.ledger.domain;

import lombok.*;
import java.io.Serializable;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class DocumentTagId implements Serializable {
  private Long document; // Document.id
  private String tag;
}