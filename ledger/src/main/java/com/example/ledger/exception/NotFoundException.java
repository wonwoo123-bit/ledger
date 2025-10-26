package com.example.ledger.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String target) {
        super(target + " not found");
    }

    public NotFoundException(String target, Throwable cause) {
        super(target + " not found", cause);
    }
}
