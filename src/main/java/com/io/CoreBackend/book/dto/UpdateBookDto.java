//package com.io.CoreBackend.demo.dto;
//
//import jakarta.validation.constraints.NotBlank;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Data
//public class UpdateBookDto {
//
//    @NotBlank(message = "Title is mandatory")
//    private String title;
//
//    @NotBlank(message = "Author is mandatory")
//    private String author;
//}


package com.io.CoreBackend.book.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBookDto {

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be at most 255 characters")
    private String title;

    @NotBlank(message = "ISBN is required")
    @Pattern(regexp = "^(97[89])?\\d{9}(\\d|X)$", message = "Invalid ISBN format")
    private String isbn;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    private BigDecimal price;

    @NotNull(message = "Stock count is required")
    @Min(value = 0, message = "Stock count cannot be negative")
    private Integer stockCount;

    private LocalDate publicationDate;

    @NotNull(message = "Author id is required")
    private Long authorId;

    private String categoryName;
}