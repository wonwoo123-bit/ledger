package com.example.ledger.service;

import java.nio.file.Path;

import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ledger.domain.Document;
import com.example.ledger.domain.User;
import com.example.ledger.dto.DocumentDto.CreateReq;
import com.example.ledger.dto.DocumentDto.DetailRes;
import com.example.ledger.dto.DocumentDto.UpdateReq;
import com.example.ledger.enums.DocumentStatus;
import com.example.ledger.enums.OcrStatus;
import com.example.ledger.ocr.OcrClient;
import com.example.ledger.repository.DocumentRepository;
import com.example.ledger.repository.UserRepository;
import com.example.ledger.storage.FileStorage;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final FileStorage fileStorage;
    private final OcrClient ocrClient;          // 실제 구현체는 나중에 주입해도 됨(스텁 가능)
    private final TaskExecutor taskExecutor;    // 비동기 실행용(또는 @Async 사용)

    @Override
    @Transactional(readOnly = true)
    public Page<DetailRes> list(Long userId, Pageable pageable) {
        return documentRepository.findByUserId(userId, pageable).map(this::toRes);
    }

    @Override
    @Transactional(readOnly = true)
    public DetailRes detail(Long id) {
        return toRes(get(id));
    }

    @Override
    public Long create(CreateReq req) {
        User user = userRepository.findById(req.userId())
                .orElseThrow(() -> new EntityNotFoundException("user not found"));

        Document doc = Document.builder()
                .user(user)
                .type(req.type())
                .payDate(req.payDate())
                .storeName(req.storeName())
                .totalAmount(req.totalAmount())
                .status(DocumentStatus.IMPORTED)
                .build();

        // 파일 저장
        if (req.receipt() != null && !req.receipt().isEmpty()) {
            Path saved = fileStorage.save(req.receipt());
            doc.setFileUrl(saved.toString());
            // OCR 상태만 미리 세팅
            doc.setOcrStatus(OcrStatus.PENDING);
        } else {
            doc.setOcrStatus(OcrStatus.NONE);
        }

        Document saved = documentRepository.save(doc);

        // 비동기 OCR 트리거
        if (req.receipt() != null && !req.receipt().isEmpty()) {
            taskExecutor.execute(() -> triggerOcr(saved.getId(), req.ocrLang()));
        }
        return saved.getId();
    }

    @Override
    public void update(UpdateReq req) {
        Document doc = get(req.id());
        if (req.type() != null) doc.setType(req.type());
        if (req.payDate() != null) doc.setPayDate(req.payDate());
        if (req.storeName() != null) doc.setStoreName(req.storeName());
        if (req.totalAmount() != null) doc.setTotalAmount(req.totalAmount());
        if (req.title() != null) doc.setTitle(req.title());
        if (req.status() != null) doc.setStatus(req.status());
        // 변경은 JPA 더티체킹으로 반영
    }

    @Override
    public void delete(Long id) {
        Document doc = get(id);
        // 첨부 삭제(선택)
        if (doc.getFileUrl() != null) fileStorage.deleteIfExists(Path.of(doc.getFileUrl()));
        documentRepository.delete(doc);
    }

    @Override
    public void triggerOcr(Long documentId, String lang) {
        Document doc = get(documentId);
        if (doc.getFileUrl() == null) return;

        try {
            doc.setOcrStatus(com.example.ledger.domain.enums.OcrStatus.PENDING);
            var result = ocrClient.recognize(Path.of(doc.getFileUrl()), lang);
            doc.setRawText(result.text());
            doc.setOcrConfidence(result.confidence());
            doc.setOcrLang(lang);
            doc.setOcrStatus(com.example.ledger.domain.enums.OcrStatus.SUCCEEDED);
        } catch (Exception e) {
            doc.setOcrStatus(com.example.ledger.domain.enums.OcrStatus.FAILED);
        }
    }

    // ===== util =====
    private Document get(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("document not found: " + id));
    }

    private DetailRes toRes(Document d) {
        return new DetailRes(
                d.getId(),
                d.getUser().getId(),
                d.getType(),
                d.getTitle(),
                d.getStoreName(),
                d.getPayDate(),
                d.getTotalAmount(),
                d.getFileUrl(),
                d.getStatus(),
                d.getRawText(),
                d.getOcrStatus() != null ? d.getOcrStatus().name() : null,
                d.getOcrConfidence(),
                d.getOcrLang()
        );
    }
}
