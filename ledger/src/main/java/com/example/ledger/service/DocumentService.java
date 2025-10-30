package com.example.ledger.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.ledger.dto.DocumentDto.CreateReq;
import com.example.ledger.dto.DocumentDto.DetailRes;
import com.example.ledger.dto.DocumentDto.UpdateReq;

public interface DocumentService {
    Page<DetailRes> list(Long userId, Pageable pageable);
    DetailRes detail(Long id);
    Long create(CreateReq req);
    void update(UpdateReq req);
    void delete(Long id);

    // OCR 비동기 트리거 (수동 재시도 용도 포함)
    void triggerOcr(Long documentId, String lang);
}
