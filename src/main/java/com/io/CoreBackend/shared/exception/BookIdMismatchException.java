package com.io.CoreBackend.shared.exception;

public class BookIdMismatchException extends RuntimeException {
    public BookIdMismatchException() {
        super("Book ID in path does not match ID in the request body");
    }

    public BookIdMismatchException(String message) {
        super(message);
    }
}