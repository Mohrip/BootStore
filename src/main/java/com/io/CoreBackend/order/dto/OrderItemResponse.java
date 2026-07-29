package com.io.CoreBackend.order.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponse {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal lineTotal;
}
