package com.github.cauebf.shoppingcartapi.request;

import java.math.BigDecimal;

import com.github.cauebf.shoppingcartapi.model.Category;

import lombok.Data;

// DTO used to receive product data from the API without exposing or coupling the Product entity to external clients

@Data // lombok annotation to generate getters and setters
public class AddProductRequest {
    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;
    private Category category;
}
