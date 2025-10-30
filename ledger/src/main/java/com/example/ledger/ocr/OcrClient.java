package com.example.ledger.ocr;

import java.nio.file.Path;

public interface OcrClient {
    record Result(String text, Double confidence) {
    }

    Result recognize(Path imagePath, String lang) throws Exception;
}
