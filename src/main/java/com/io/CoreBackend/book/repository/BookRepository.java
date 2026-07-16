package com.io.CoreBackend.book.repository;

import com.io.CoreBackend.book.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Override
    @EntityGraph(attributePaths = {"author", "category"})
    Page<Book> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"author", "category"})
    Optional<Book> findById(Long id);


    @EntityGraph(attributePaths = {"author", "category"})
    Page<Book> findByTitle(String title, Pageable pageable);

    boolean existsByIsbn(String isbn);

    boolean existsByIsbnAndIdNot(String isbn, Long id);

    @EntityGraph(attributePaths = {"author", "category"})
    Page<Book> findByCategoryName(String categoryName, Pageable pageable);

    @EntityGraph(attributePaths = {"author", "category"})
    Page<Book> findByAuthorName(String authorName, Pageable pageable);
}