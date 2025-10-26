package com.example.ledger.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ledger.domain.Document;
import com.example.ledger.domain.User;
import com.example.ledger.dto.DocumentDtos.DocumentCreateReq;
import com.example.ledger.dto.DocumentDtos.DocumentRes;
import com.example.ledger.dto.DocumentDtos.DocumentUpdateReq;
import com.example.ledger.exception.NotFoundException;
import com.example.ledger.repository.DocumentRepository;
import com.example.ledger.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentService {
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public DocumentRes create(DocumentCreateReq req) {
        User user = userRepository.findById(req.userId())
                .orElseThrow(() -> new NotFoundException("user"));

        Document d = new Document();
        d.setUser(user);
        d.setType(Document.Type.valueOf(req.type()));
        d.setTitle(req.title());
        d.setStoreName(req.storeName());
        d.setPayDate(req.payDate());
        d.setTotalAmount(req.totalAmount());
        d.setPaymentMethod(Document.PaymentMethod.valueOf(
                Optional.ofNullable(req.paymentMethod()).orElse("UNKNOWN")));
        d.setFileUrl(req.fileUrl());
        d.setStatus(Document.Status.IMPORTED);

        Document saved = documentRepository.save(d); // INSERT
        return DocumentRes.of(saved);
    }

    @Transactional(readOnly = true)
    public DocumentRes getOne(Long id, Long userId) {
        Document d = documentRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new NotFoundException("document"));
        return DocumentRes.of(d);
    }

    @Transactional(readOnly = true)
    public Page<DocumentRes> list(Long userId, LocalDate start, LocalDate end, Pageable pageable) {
        Page<Document> page = (start != null && end != null)
                ? documentRepository.findByUserIdAndDateBetween(userId, start, end, pageable)
                : documentRepository.findByUserId(userId, pageable);
        return page.map(DocumentRes::of);
    }

    @Transactional
    public DocumentRes update(Long id, Long userId, DocumentUpdateReq req) {
        Document d = documentRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new NotFoundException("document"));

        // 변경 감지(더티 체크): setter만 호출하면 트랜잭션 종료 시 자동 UPDATE
        if (req.title() != null)
            d.setTitle(req.title());
        if (req.storeName() != null)
            d.setStoreName(req.storeName());
        if (req.payDate() != null)
            d.setPayDate(req.payDate());
        if (req.totalAmount() != null)
            d.setTotalAmount(req.totalAmount());
        if (req.paymentMethod() != null)
            d.setPaymentMethod(Document.PaymentMethod.valueOf(req.paymentMethod()));
        if (req.status() != null)
            d.setStatus(Document.Status.valueOf(req.status()));

        return DocumentRes.of(d); // save() 불필요 (영속 상태)
    }

    @Transactional
    public void delete(Long id, Long userId) {
        Document d = documentRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new NotFoundException("document"));
        documentRepository.delete(d); // DELETE
    }

}
