package com.io.CoreBackend.book.dto;

import com.io.CoreBackend.shared.validation.ValidIsbn;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookDto {

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be at most 255 characters")
    private String title;

    @NotBlank(message = "ISBN is required")
    @ValidIsbn
    private String isbn;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    @DecimalMax(value = "99999999.99", message = "Price must not exceed 99999999.99")
    @Digits(integer = 8, fraction = 2, message = "Price must have at most 8 integer digits and 2 decimals")
    private BigDecimal price;

    @NotNull(message = "Stock count is required")
    @Min(value = 0, message = "Stock count cannot be negative")
    @Max(value = 1_000_000, message = "Stock count must not exceed 1000000")
    private Integer stockCount;

    @PastOrPresent(message = "Publication date cannot be in the future")
    private LocalDate publicationDate;

    @NotNull(message = "Author id is required")
    private Long authorId;

    /**
     * Optional. A book may have no category (the FK is nullable and ~10% of
     * seeded books have none). Blank or null means "no category".
     */
    @Size(max = 255, message = "Category name must be at most 255 characters")
    private String categoryName;

}
