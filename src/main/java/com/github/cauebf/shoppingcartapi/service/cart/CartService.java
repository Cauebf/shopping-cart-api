package com.github.cauebf.shoppingcartapi.service.cart;

import org.springframework.stereotype.Service;

import com.github.cauebf.shoppingcartapi.exceptions.ResourceNotFoundException;
import com.github.cauebf.shoppingcartapi.model.Cart;
import com.github.cauebf.shoppingcartapi.repository.CartRepository;

@Service
public class CartService implements ICartService {

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        // construtor dependency injection
        this.cartRepository = cartRepository; 
    }

    @Override
    public Cart getCart(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found!"));
    }

    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        cart.clear();
        cartRepository.save(cart);
    }

    @Override
    public void deleteCart(Long id) {
        cartRepository.findById(id).ifPresentOrElse(cartRepository::delete, () -> { // if found, delete
            throw new ResourceNotFoundException("Cart not found!"); // if not found, throw an exception
        }); 
    }
}
