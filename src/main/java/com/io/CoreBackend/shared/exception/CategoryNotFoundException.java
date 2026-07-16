package com.io.CoreBackend.shared.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String name) {
        super("Category not found with name: " + name);
    }
}
