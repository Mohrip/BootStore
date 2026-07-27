package com.io.CoreBackend.shared.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validates that a value is a structurally correct ISBN-10 or ISBN-13,
 * including the check digit. Null values pass; combine with @NotBlank
 * when the field is required.
 */
@Documented
@Constraint(validatedBy = IsbnValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidIsbn {

    String message() default "Invalid ISBN (check digit does not match)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
