package com.io.CoreBackend.cart.repository;

import com.io.CoreBackend.cart.entity.Cart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @EntityGraph(attributePaths = {"items", "items.book"})
    Optional<Cart> findByCustomerId(Long customerId);
}