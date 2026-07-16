package com.io.CoreBackend.authors.repository;

import com.io.CoreBackend.authors.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}