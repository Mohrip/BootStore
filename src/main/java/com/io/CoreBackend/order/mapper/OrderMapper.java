package com.io.CoreBackend.order.mapper;

import com.io.CoreBackend.order.dto.OrderItemResponse;
import com.io.CoreBackend.order.dto.OrderResponse;
import com.io.CoreBackend.order.entity.Order;
import com.io.CoreBackend.order.entity.OrderItem;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderItemResponse toItemResponse(OrderItem item) {
        return OrderItemResponse.builder()
                .id(item.getId())
                .bookId(item.getBook() != null ? item.getBook().getId() : null)
                .bookTitle(item.getBookTitle())
                .unitPrice(item.getUnitPrice())
                .quantity(item.getQuantity())
                .lineTotal(item.getLineTotal())
                .build();
    }

    public OrderResponse toResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .items(order.getItems().stream().map(this::toItemResponse).toList())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .placedAt(order.getCreatedAt())
                .build();
    }
}