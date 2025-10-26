package com.example.ledger.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.ledger.dto.DocumentDtos.DocumentCreateReq;
import com.example.ledger.dto.DocumentDtos.DocumentRes;
import com.example.ledger.dto.DocumentDtos.DocumentUpdateReq;
import com.example.ledger.service.DocumentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {
    @Autowired
    private DocumentService documentService;

     @PostMapping
  public ResponseEntity<DocumentRes> create(@RequestBody @Valid DocumentCreateReq req){
    return ResponseEntity.status(HttpStatus.CREATED).body(documentService.create(req));
  }

  @GetMapping("/{id}")
  public DocumentRes getOne(@PathVariable Long id, @RequestParam Long userId){
    return documentService.getOne(id, userId);
  }

  @GetMapping
  public Page<DocumentRes> list(
      @RequestParam Long userId,
      @RequestParam(required=false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
      @RequestParam(required=false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
      @PageableDefault(size=20, sort="payDate", direction=Sort.Direction.DESC) Pageable pageable){
    return documentService.list(userId, start, end, pageable);
  }

  @PutMapping("/{id}")
  public DocumentRes update(@PathVariable Long id,
                            @RequestParam Long userId,
                            @RequestBody DocumentUpdateReq req){
    return documentService.update(id, userId, req);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id, @RequestParam Long userId){
    documentService.delete(id, userId);
  }

}
