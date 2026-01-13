package com.github.cauebf.shoppingcartapi.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class CartDto {
    private Long id;
    private BigDecimal totalAmount;
    private List<CartItemDto> items;
}
