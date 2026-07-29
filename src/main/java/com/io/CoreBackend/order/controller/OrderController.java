package com.io.CoreBackend.order.controller;

import com.io.CoreBackend.order.dto.OrderResponse;
import com.io.CoreBackend.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout(Authentication auth) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.checkout(auth.getName()));
    }

    @GetMapping
    public Page<OrderResponse> getHistory(Authentication auth,
                                          @PageableDefault(size = 20) Pageable pageable) {
        return orderService.getOrderHistory(auth.getName(), pageable);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(Authentication auth,
                                                  @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrder(auth.getName(), orderId));
    }
}