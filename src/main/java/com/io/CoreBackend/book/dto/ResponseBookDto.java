package com.io.CoreBackend.book.dto;

import com.io.CoreBackend.authors.dto.AuthorResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseBookDto {
    private Long id;
    private String title;
    private String isbn;
    private BigDecimal price;
    private Integer stockCount;
    private LocalDate publicationDate;
    private AuthorResponseDto author;
    private CategoryResponseDto category;
}