package com.github.cauebf.shoppingcartapi.model;

import java.math.BigDecimal;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;

    // many cart items to one product
    @ManyToOne
    @JoinColumn(name = "product_id") // foreign key
    private Product product;
    
    // many cart items to one cart
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id") // foreign key
    private Cart cart;

    public void setTotalPrice() {
        // multiply unit price by quantity
        this.totalPrice = this.unitPrice.multiply(new BigDecimal(quantity));
    }
}
