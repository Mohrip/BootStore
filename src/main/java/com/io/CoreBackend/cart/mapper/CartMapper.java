package com.io.CoreBackend.cart.mapper;


import com.io.CoreBackend.cart.dto.CartItemResponse;
import com.io.CoreBackend.cart.dto.CartResponse;
import com.io.CoreBackend.cart.entity.Cart;
import com.io.CoreBackend.cart.entity.CartItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CartMapper {

    public CartItemResponse toItemResponse(CartItem item) {
        BigDecimal unitPrice = item.getBook().getPrice();
        return CartItemResponse.builder()
                .id(item.getId())
                .bookId(item.getBook().getId())
                .bookTitle(item.getBook().getTitle())
                .unitPrice(unitPrice)
                .quantity(item.getQuantity())
                .lineTotal(unitPrice.multiply(BigDecimal.valueOf(item.getQuantity())))
                .availableStock(item.getBook().getStockCount())
                .build();
    }

    public CartResponse toResponse(Cart cart) {
        List<CartItemResponse> items = cart.getItems().stream()
                .map(this::toItemResponse)
                .toList();


        BigDecimal total = items.stream()
                .map(CartItemResponse::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        int totalItems = items.stream()
                .mapToInt(CartItemResponse::getQuantity)
                .sum();


        return CartResponse.builder()
                .id(cart.getId())
                .items(items)
                .totalAmount(total)
                .totalItems(totalItems)
                .build();
    }
}