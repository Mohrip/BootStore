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
}
