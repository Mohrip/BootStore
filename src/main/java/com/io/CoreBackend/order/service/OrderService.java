package com.io.CoreBackend.order.service;

import com.io.CoreBackend.book.entity.Book;
import com.io.CoreBackend.book.service.BookService;
import com.io.CoreBackend.cart.entity.Cart;
import com.io.CoreBackend.cart.entity.CartItem;
import com.io.CoreBackend.cart.service.CartService;
import com.io.CoreBackend.customer.entity.Customer;
import com.io.CoreBackend.customer.service.CustomerService;
import com.io.CoreBackend.order.dto.OrderResponse;
import com.io.CoreBackend.order.entity.Order;
import com.io.CoreBackend.order.entity.OrderItem;
import com.io.CoreBackend.order.entity.OrderStatus;
import com.io.CoreBackend.order.mapper.OrderMapper;
import com.io.CoreBackend.order.repository.OrderRepository;
import com.io.CoreBackend.shared.exception.BusinessRuleException;
import com.io.CoreBackend.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {


    private final OrderRepository orderRepository;
    private final BookService bookService;
    private final CustomerService customerService;
    private final CartService cartService;
    private final OrderMapper orderMapper;


    public OrderResponse checkout(String customerEmail) {
        Customer customer = customerService.findEntityByEmail(customerEmail);
        Cart cart = cartService.getCartEntity(customerEmail);
        if (cart.getItems().isEmpty()) {
            throw new BusinessRuleException("Cannot place an order with an empty cart");
        }

        // Lock + Verify + decrement stock
        Map<Long, Integer> quantities = new LinkedHashMap<>();
        for (CartItem cartItem : cart.getItems()) {
            quantities.put(cartItem.getId(), cartItem.getQuantity());
        }
        Map<Long, Book> lockedBooks = bookService.reserveStock(quantities);

        // Build the order
        Order order = Order.builder()
                .customer(customer)
                .status(OrderStatus.PENDING)
                .totalAmount(BigDecimal.ZERO)
                .build();

        BigDecimal total = BigDecimal.ZERO;
        for (CartItem cartItem : cart.getItems()) {
            Book book = lockedBooks.get(cartItem.getBook().getId());
            BigDecimal unitPrice = book.getPrice();
            BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            order.addItem(OrderItem.builder()
                    .book(book)
                    .bookTitle(book.getTitle())
                    .unitPrice(unitPrice)
                    .quantity(cartItem.getQuantity())
                    .lineTotal(lineTotal)
                    .build());

            total = total.add(lineTotal);
        }
        order.setTotalAmount(total);

        Order saved = orderRepository.save(order);


    // Empty the cart (orphanRemoval deletes the rows).
        cart.getItems().clear();

        log.info("Order {} placed by customer {} for {}", saved.getId(), customer.getId(), total);
        return orderMapper.toResponse(saved);
}

@Transactional(readOnly = true)
public Page<OrderResponse> getOrderHistory(String customerEmail, Pageable pageable) {
    Customer customer = customerService.findEntityByEmail(customerEmail);
    return orderRepository
            .findByCustomerIdOrderByCreatedAtDesc(customer.getId(), pageable)
            .map(orderMapper::toResponse); // هنا قاعدين نسوي معادلة وربط الاجابة بهيكل تناقل البيانات
}

@Transactional(readOnly = true)
public OrderResponse getOrder(String customerEmail, Long orderId) {
    Customer customer = customerService.findEntityByEmail(customerEmail);
    return orderRepository.findByIdAndCustomerId(orderId, customer.getId())
            .map(orderMapper::toResponse)
            .orElseThrow(() -> new ResourceNotFoundException("Order", orderId));
}


}
