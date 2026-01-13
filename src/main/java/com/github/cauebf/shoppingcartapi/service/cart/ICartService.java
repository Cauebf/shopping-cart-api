package com.github.cauebf.shoppingcartapi.service.cart;

import com.github.cauebf.shoppingcartapi.dto.CartDto;
import com.github.cauebf.shoppingcartapi.model.Cart;
import com.github.cauebf.shoppingcartapi.model.User;

public interface ICartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    void deleteCart(Long id);
    Cart getCartByUserId(Long userId);
    Cart findOrCreateCartByUser(User user);
    CartDto convertToDto(Cart cart);
}
