package com.io.CoreBackend.shared.exception;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException() {
        super("Book not found");
    }

    public BookNotFoundException(String message) {
        super(message);
    }

    public BookNotFoundException(Long id) {
        super("Book not found with id: " + id);
    }

    public BookNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookNotFoundException(String title, boolean isTitle) {
        super("Book not found with title: " + title);
    }
}