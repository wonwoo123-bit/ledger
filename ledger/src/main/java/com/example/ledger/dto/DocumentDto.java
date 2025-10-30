package com.example.ledger.dto;

import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

import com.example.ledger.enums.DocumentStatus;
import com.example.ledger.enums.DocumentType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

public class DocumentDto {

    public record CreateReq(
            @NotNull Long userId,
            @NotNull DocumentType type,
            @NotNull @PastOrPresent LocalDate payDate,
            @NotBlank String storeName,
            @PositiveOrZero Long totalAmount,
            // 선택: categoryId, title 등 필요 시 추가
            MultipartFile receipt,      // 첨부파일(선택)
            String ocrLang              // 예: "kor+eng"
    ) {}

    public record UpdateReq(
            @NotNull Long id,
            DocumentType type,
            @PastOrPresent LocalDate payDate,
            String storeName,
            @PositiveOrZero Long totalAmount,
            String title,
            DocumentStatus status
    ) {}

    public record DetailRes(
            Long id, Long userId, DocumentType type, String title, String storeName,
            LocalDate payDate, Long totalAmount, String fileUrl,
            DocumentStatus status, String rawText, String ocrStatus, Double ocrConfidence, String ocrLang
    ) {}
}
