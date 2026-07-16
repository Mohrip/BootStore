package com.io.CoreBackend.book.mapper;

import com.io.CoreBackend.authors.dto.AuthorResponseDto;
import com.io.CoreBackend.book.dto.*;
import com.io.CoreBackend.authors.entity.Author;
import com.io.CoreBackend.book.entity.Book;
import com.io.CoreBackend.book.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public Book toEntity(CreateBookDto dto, Author author, Category category) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setPrice(dto.getPrice());
        book.setStockCount(dto.getStockCount());
        book.setPublicationDate(dto.getPublicationDate());
        book.setAuthor(author);
        book.setCategory(category);
        return book;
    }

    public void updateEntityFromDto(UpdateBookDto dto, Book book, Author author, Category category) {
        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setPrice(dto.getPrice());
        book.setStockCount(dto.getStockCount());
        book.setPublicationDate(dto.getPublicationDate());
        book.setAuthor(author);
        book.setCategory(category);
    }

    public ResponseBookDto toResponseDto(Book book) {
        AuthorResponseDto authorDto = new AuthorResponseDto(book.getAuthor().getId(), book.getAuthor().getName());
        CategoryResponseDto categoryDto = book.getCategory() != null
                ? new CategoryResponseDto(book.getCategory().getId(), book.getCategory().getName())
                : null;
        return new ResponseBookDto(
                book.getId(), book.getTitle(), book.getIsbn(), book.getPrice(),
                book.getStockCount(), book.getPublicationDate(), authorDto, categoryDto
        );
    }
}