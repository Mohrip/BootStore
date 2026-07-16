package com.io.CoreBackend.book.repository;

import com.io.CoreBackend.book.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> getCategoriesByName(String name);
}
