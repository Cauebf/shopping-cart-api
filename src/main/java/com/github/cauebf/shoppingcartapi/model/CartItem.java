package com.github.cauebf.shoppingcartapi.model;

import java.math.BigDecimal;

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

    // many cart items to one product
    @ManyToOne
    @JoinColumn(name = "product_id") // foreign key
    private Product product;
    
    // many cart items to one cart
    @ManyToOne
    @JoinColumn(name = "cart_id") // foreign key
    private Cart cart;

    public CartItem(Product product, Cart cart) {
        this.product = product;
        this.cart = cart;
        this.unitPrice = product.getPrice();
        this.quantity = 0;
    }

    public void increaseQuantity(int amount) {
        this.quantity += amount;
    }

    public BigDecimal getTotalPrice() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
