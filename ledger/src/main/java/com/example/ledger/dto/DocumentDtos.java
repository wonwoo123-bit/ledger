package com.example.ledger.dto;

import java.time.LocalDate;

import com.example.ledger.domain.Document;

public class DocumentDtos {
    
public record DocumentCreateReq(
  Long userId,
  String type,            // "INCOME" / "EXPENSE"
  String title,
  String storeName,
  LocalDate payDate,
  Long totalAmount,
  String paymentMethod,   // "CARD"/"CASH"/"UNKNOWN"
  String fileUrl
) {}

public record DocumentUpdateReq(
  String title,
  String storeName,
  LocalDate payDate,
  Long totalAmount,
  String paymentMethod,
  String status           // "IMPORTED"/"CONFIRMED"
) {}

public record DocumentRes(
  Long id, Long userId, String type, String title, String storeName,
  LocalDate payDate, Long totalAmount, String paymentMethod, String status
) {
  public static DocumentRes of(Document d){
    return new DocumentRes(
      d.getId(), d.getUser().getId(), d.getType().name(), d.getTitle(), d.getStoreName(),
      d.getPayDate(), d.getTotalAmount(),
      d.getPaymentMethod() == null ? null : d.getPaymentMethod().name(),
      d.getStatus().name()
    );
  }
}


}
