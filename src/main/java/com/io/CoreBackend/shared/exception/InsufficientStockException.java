package com.io.CoreBackend.shared.exception;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String bookTitle, int available, int requested) {
        super("Insufficient stock for '" + bookTitle + "': requested "
                + requested + ", available " + available);
    }
}