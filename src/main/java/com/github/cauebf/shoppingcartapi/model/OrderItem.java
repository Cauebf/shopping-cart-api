package com.github.cauebf.shoppingcartapi.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "product_id") // foreign key
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id") // foreign key
    private Order order;

    public OrderItem(Product product, Order order, int quantity, BigDecimal price) {
        this.product = product;
        this.order = order;
        this.quantity = quantity;
        this.price = price;
    }
}
