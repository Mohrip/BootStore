package com.io.CoreBackend.shared.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String title, int available, int requested) {
        super(String.format("Insufficient stock for '%s'. Available: %d, Requested: %d",
                title, available, requested));
    }
}
