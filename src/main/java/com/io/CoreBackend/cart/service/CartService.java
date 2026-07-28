package com.io.CoreBackend.cart.service;

import com.io.CoreBackend.book.entity.Book;
import com.io.CoreBackend.book.service.BookService;
import com.io.CoreBackend.cart.dto.*;
import com.io.CoreBackend.cart.entity.Cart;
import com.io.CoreBackend.cart.entity.CartItem;
import com.io.CoreBackend.cart.mapper.CartMapper;
import com.io.CoreBackend.cart.repository.CartRepository;
import com.io.CoreBackend.customer.entity.Customer;
import com.io.CoreBackend.customer.service.CustomerService;
import com.io.CoreBackend.shared.exception.InsufficientStockException;
import com.io.CoreBackend.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final BookService bookService;
    private final CustomerService customerService;

    @Transactional(readOnly = true)
    public CartResponse getCart(String customerEmail) {
        return cartMapper.toResponse(findOrCreateCart(customerEmail));
    }

    public CartResponse addItem(String customerEmail, AddCartItemRequest request) {
        Cart cart = findOrCreateCart(customerEmail);
        Book book = bookService.findEntityById(request.getBookId());

        Optional<CartItem> existing = cart.getItems().stream()
                .filter(i -> i.getBook().getId().equals(book.getId()))
                .findFirst();

        // Validate the RESULTING total, not just the delta.
        int newQuantity = existing.map(CartItem::getQuantity).orElse(0) + request.getQuantity();
        requireStock(book, newQuantity);

        if (existing.isPresent()) {
            existing.get().setQuantity(newQuantity);
        } else {
            cart.addItem(CartItem.builder()
                    .book(book)
                    .quantity(request.getQuantity())
                    .build());
        }

        log.info("Cart {}: book {} -> qty {}", cart.getId(), book.getId(), newQuantity);
        return cartMapper.toResponse(cartRepository.save(cart));
    }

    public CartResponse updateItem(String customerEmail, Long bookId, UpdateCartItemRequest request) {
        Cart cart = findOrCreateCart(customerEmail);

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getBook().getId().equals(bookId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cart item for book", bookId));

        requireStock(item.getBook(), request.getQuantity());
        item.setQuantity(request.getQuantity());

        return cartMapper.toResponse(cartRepository.save(cart));
    }

    public CartResponse removeItem(String customerEmail, Long bookId) {
        Cart cart = findOrCreateCart(customerEmail);

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getBook().getId().equals(bookId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cart item for book", bookId));

        cart.removeItem(item);
        return cartMapper.toResponse(cartRepository.save(cart));
    }

    public void clearCart(String customerEmail) {
        Cart cart = findOrCreateCart(customerEmail);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    public Cart getCartEntity(String customerEmail) {
        return findOrCreateCart(customerEmail);
    }

    private Cart findOrCreateCart(String customerEmail) {
        Customer customer = customerService.findEntityByEmail(customerEmail);
        return cartRepository.findByCustomerId(customer.getId())
                .orElseGet(() -> cartRepository.save(
                        Cart.builder().customer(customer).build()));
    }

    private void requireStock(Book book, int requested) {
        if (book.getStockCount() < requested) {
            throw new InsufficientStockException(
                    book.getTitle(), book.getStockCount(), requested);
        }
    }
}