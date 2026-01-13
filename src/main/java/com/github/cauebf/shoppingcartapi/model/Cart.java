package com.github.cauebf.shoppingcartapi.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal totalAmount = BigDecimal.ZERO; // default value

    // one cart to many cart items
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true) // if delete cart, delete cart items
    private Set<CartItem> items = new HashSet<>();

    // one cart to one user
    @OneToOne
    @JoinColumn(name = "user_id") // foreign key
    private User user;

    public void recalculateTotal() {
        this.totalAmount = items.stream()
                .map(CartItem::getTotalPrice) // get the total price of each cart item
                .reduce(BigDecimal.ZERO, BigDecimal::add); // sum the total prices starting from zero
    }
}
