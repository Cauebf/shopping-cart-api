package com.github.cauebf.shoppingcartapi.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.github.cauebf.shoppingcartapi.exceptions.ResourceNotFoundException;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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

    public void addItem(Product product, int quantity) {
        CartItem item = findItem(product.getId()) // check if the product already exists in the cart
                .orElseGet(() -> { // if not, create a new cartItem and add it to the cart
                    CartItem newItem = new CartItem(product, this);
                    items.add(newItem); // add the new cartItem to the cart, which will save the cartItem to the DB because of the cascade
                    return newItem;
                });

        item.increaseQuantity(quantity);
        recalculateTotal();
    }

    public void updateItemQuantity(Long productId, int quantity) {
        // if the quantity is 0, remove the item from the cart
        if (quantity <= 0) {
            removeItem(productId);
            return;
        }

        CartItem item = findItem(productId) // check if the product exists in the cart
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart item not found!")
                );

        item.updateQuantity(quantity); // update the quantity
        recalculateTotal();
    }

    public void removeItem(Long productId) {
        CartItem item = findItem(productId) // check if the product exists in the cart
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart item not found!")
                );

        items.remove(item);
        recalculateTotal();
    }

    public void clear() {
        items.clear();
        recalculateTotal();
    }

    private Optional<CartItem> findItem(Long productId) {
        return items.stream()
                .filter(i -> i.getProduct().getId().equals(productId)) // check if the product exists in the cart
                .findFirst();
    }

    private void recalculateTotal() {
        this.totalAmount = items.stream()
                .map(CartItem::getTotalPrice) // get the total price of each cart item
                .reduce(BigDecimal.ZERO, BigDecimal::add); // sum the total prices starting from zero
    }
}
