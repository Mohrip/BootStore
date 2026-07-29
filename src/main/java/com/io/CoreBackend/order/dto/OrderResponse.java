package com.io.CoreBackend.order.dto;

import com.io.CoreBackend.order.entity.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.time.LocalDateTime;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private Long id;
    private List<OrderItemResponse> items;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private LocalDateTime placedAt;
}
