package com.io.CoreBackend.cart.controller;

import com.io.CoreBackend.cart.dto.*;
import com.io.CoreBackend.cart.repository.CartRepository;
import com.io.CoreBackend.cart.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CartRepository cartRepository;

    @GetMapping
    public ResponseEntity<CartResponse> getCart(Authentication auth) {
        return ResponseEntity.ok(cartService.getCart(auth.getName()));
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponse> addItem(Authentication auth, @Valid @RequestBody AddCartItemRequest addCartItemRequest) {
        return ResponseEntity.ok(cartService.addItem(auth.getName(), addCartItemRequest));
    }

    @PutMapping("/items/{bookId}")
    public ResponseEntity<CartResponse> updateItem(Authentication auth, @PathVariable Long bookId, @Valid @RequestBody UpdateCartItemRequest updateCartItemRequest) {
        return ResponseEntity.ok(cartService.updateItem(auth.getName(), bookId, updateCartItemRequest));
    }

    @DeleteMapping("/items/{bookId}")
    public ResponseEntity<CartResponse> removeItem(Authentication auth,
                                                   @PathVariable Long bookId) {
        return ResponseEntity.ok(cartService.removeItem(auth.getName(), bookId));
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart(Authentication auth) {
        cartService.clearCart(auth.getName());
        return ResponseEntity.noContent().build();
    }


}
