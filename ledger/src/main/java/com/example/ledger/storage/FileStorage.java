package com.example.ledger.storage;

import java.nio.file.Path;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorage {
    Path save(MultipartFile file); // 저장 후 물리 경로 반환

    void deleteIfExists(Path path);
}
