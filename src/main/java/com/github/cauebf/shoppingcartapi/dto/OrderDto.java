package com.github.cauebf.shoppingcartapi.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class OrderDto {
    private Long id;
    private LocalDate orderDate;
    private BigDecimal totalAmount;
    private String orderStatus;
    private Long userId;
    private List<OrderItemDto> orderItems;
}
