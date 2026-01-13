package com.github.cauebf.shoppingcartapi.service.cart;

import com.github.cauebf.shoppingcartapi.dto.CartItemDto;
import com.github.cauebf.shoppingcartapi.model.CartItem;

public interface ICartItemService {
    void addItemToCart(Long cartId, Long productId, int quantity);
    void removeItemFromCart(Long cartId, Long productId);
    CartItem getCartItem(Long cartId, Long productId);
    void updateItemQuantity(Long cartId, Long productId, int quantity);
    CartItemDto convertToDto(CartItem cartItem);
}
