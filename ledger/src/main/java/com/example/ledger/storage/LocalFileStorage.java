package com.example.ledger.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LocalFileStorage implements FileStorage {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Override
    public Path save(MultipartFile file) {
        try {
            Path dir = Path.of(uploadDir);
            Files.createDirectories(dir);
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path target = dir.resolve(filename).normalize();
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return target.toAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException("File save failed", e);
        }
    }

    @Override
    public void deleteIfExists(Path path) {
        try {
            if (path != null)
                Files.deleteIfExists(path);
        } catch (IOException ignore) {
        }
    }
}
