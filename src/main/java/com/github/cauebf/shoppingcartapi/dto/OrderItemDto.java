package com.github.cauebf.shoppingcartapi.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderItemDto {
    private int quantity;
    private BigDecimal price;
    private Long productId;
    private String productName;
}
