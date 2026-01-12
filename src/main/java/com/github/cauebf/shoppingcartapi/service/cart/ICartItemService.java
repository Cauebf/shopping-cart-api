package com.github.cauebf.shoppingcartapi.service.cart;

import com.github.cauebf.shoppingcartapi.model.CartItem;

public interface ICartItemService {
    void addCartItem(Long cartId, Long productId, int quantity);
    void removeItemFromCart(Long cartId, Long productId);
    CartItem getCartItem(Long cartId, Long productId);
    void updateItemQuantity(Long cartId, Long productId, int quantity);
}
