package com.io.CoreBackend.shared.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IsbnValidator implements ConstraintValidator<ValidIsbn, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            // Emptiness is @NotBlank's job, not ours.
            return true;
        }

        String normalized = value.replace("-", "").replace(" ", "").toUpperCase();

        return switch (normalized.length()) {
            case 10 -> isValidIsbn10(normalized);
            case 13 -> isValidIsbn13(normalized);
            default -> false;
        };
    }

    /** ISBN-10: sum of digit*(10-position) must be divisible by 11; final char may be 'X' (=10). */
    private boolean isValidIsbn10(String isbn) {
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            char c = isbn.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
            sum += (c - '0') * (10 - i);
        }

        char checkChar = isbn.charAt(9);
        int checkDigit;
        if (checkChar == 'X') {
            checkDigit = 10;
        } else if (checkChar >= '0' && checkChar <= '9') {
            checkDigit = checkChar - '0';
        } else {
            return false;
        }

        return (sum + checkDigit) % 11 == 0;
    }

    /** ISBN-13: alternating 1x/3x weights must sum to a multiple of 10; prefix must be 978/979. */
    private boolean isValidIsbn13(String isbn) {
        if (!isbn.startsWith("978") && !isbn.startsWith("979")) {
            return false;
        }

        int sum = 0;
        for (int i = 0; i < 13; i++) {
            char c = isbn.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
            int digit = c - '0';
            sum += (i % 2 == 0) ? digit : digit * 3;
        }

        return sum % 10 == 0;
    }
}
