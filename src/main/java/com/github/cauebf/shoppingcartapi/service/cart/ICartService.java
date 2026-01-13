package com.github.cauebf.shoppingcartapi.service.cart;

import com.github.cauebf.shoppingcartapi.dto.CartDto;
import com.github.cauebf.shoppingcartapi.model.Cart;

public interface ICartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    void deleteCart(Long id);
    CartDto convertToDto(Cart cart);
}
