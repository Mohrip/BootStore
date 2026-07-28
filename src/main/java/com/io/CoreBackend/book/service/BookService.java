package com.io.CoreBackend.book.service;

import com.io.CoreBackend.book.dto.CreateBookDto;
import com.io.CoreBackend.book.dto.ResponseBookDto;
import com.io.CoreBackend.book.dto.UpdateBookDto;
import com.io.CoreBackend.authors.entity.Author;
import com.io.CoreBackend.book.entity.Book;
import com.io.CoreBackend.book.entity.Category;
import com.io.CoreBackend.book.mapper.BookMapper;
import com.io.CoreBackend.authors.repository.AuthorRepository;
import com.io.CoreBackend.book.repository.BookRepository;
import com.io.CoreBackend.book.repository.CategoryRepository;
import com.io.CoreBackend.shared.exception.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final BookMapper bookMapper;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository,
                       CategoryRepository categoryRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
        this.bookMapper = bookMapper;
    }

    @Transactional(readOnly = true)
    public Page<ResponseBookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).map(bookMapper::toResponseDto);
    }

    @Transactional(readOnly = true)
    public Page<ResponseBookDto> findByTitle(String title, Pageable pageable) {
        return bookRepository.findByTitle(title, pageable).map(bookMapper::toResponseDto);
    }


    @Transactional(readOnly = true)
    public Optional<ResponseBookDto> findById(Long id) {
        return bookRepository.findById(id).map(bookMapper::toResponseDto);
    }

    @Transactional
    public ResponseBookDto create(CreateBookDto dto) {
        if (bookRepository.existsByIsbn(dto.getIsbn())) {
            throw new BookAlreadyExistsException(dto.getIsbn());
        }
        Author author = authorRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new AuthorNotFoundException(dto.getAuthorId()));
        Category category = resolveCategory(dto.getCategoryName());

        Book book = bookMapper.toEntity(dto, author, category);
        Book saved = bookRepository.save(book);
        return bookMapper.toResponseDto(saved);
    }

    @Transactional
    public ResponseBookDto update(Long id, UpdateBookDto dto) {
        Book existing = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        if (bookRepository.existsByIsbnAndIdNot(dto.getIsbn(), id)) {
            throw new BookAlreadyExistsException(dto.getIsbn());
        }
        Author author = authorRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new AuthorNotFoundException(dto.getAuthorId()));
        Category category = resolveCategory(dto.getCategoryName());

        bookMapper.updateEntityFromDto(dto, existing, author, category);
        return bookMapper.toResponseDto(existing);
    }

    @Transactional
    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException(id);
        }
        bookRepository.deleteById(id);
    }

    private Category resolveCategory(String categoryName) {
        if (categoryName == null || categoryName.isBlank()) {
            return null;
        }
        return categoryRepository.getCategoriesByName(categoryName)
                .orElseThrow(() -> new CategoryNotFoundException(categoryName));
    }


    @Transactional(readOnly = true)
    public Book findEntityById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    /**
     *
     * @param quantities bookId -> quantity requested
     * @return the locked books, keyed by id
     */
    @Transactional
    public Map<Long, Book> reserveStock(Map<Long, Integer> quantities) {
        List<Book> books = bookRepository.findAllByIdForUpdate(quantities.keySet());

        if(books.size() != quantities.size()) {
            throw new BookNotFoundException(0L);
        }

        for (Book book : books) {
            int requested =  quantities.get(book.getId());
            if (book.getStockCount() < requested) {
                throw new InsufficientStockException(book.getTitle(), book.getStockCount(), requested);
            }
            book.setStockCount(book.getStockCount() - requested);
        }
        return books.stream().collect(Collectors.toMap(Book::getId, book -> book));

    }
}