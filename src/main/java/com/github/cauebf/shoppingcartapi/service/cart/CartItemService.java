package com.github.cauebf.shoppingcartapi.service.cart;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.github.cauebf.shoppingcartapi.dto.CartItemDto;
import com.github.cauebf.shoppingcartapi.exceptions.ResourceNotFoundException;
import com.github.cauebf.shoppingcartapi.model.Cart;
import com.github.cauebf.shoppingcartapi.model.CartItem;
import com.github.cauebf.shoppingcartapi.model.Product;
import com.github.cauebf.shoppingcartapi.repository.CartRepository;
import com.github.cauebf.shoppingcartapi.service.product.IProductService;

@Service
public class CartItemService implements ICartItemService {
    private final IProductService productService;
    private final ICartService cartService;
    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;

    public CartItemService(
        IProductService productService, 
        ICartService cartService, 
        CartRepository cartRepository, 
        ModelMapper modelMapper
    ) {
        // construtor dependency injection
        this.productService = productService;
        this.cartService = cartService;
        this.cartRepository = cartRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CartItem getCartItem(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        return cart.getItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId)) // for each cart item, if the product ID matches, return it
                .findFirst() 
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found!")); // if not found, throw an exception
    }

    @Override
     public void addCartItem(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        Product product = productService.getProductById(productId);

        cart.addItem(product, quantity);

        cartRepository.save(cart);
    }

    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);

        cart.updateItemQuantity(productId, quantity);

        cartRepository.save(cart);
    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);

        cart.removeItem(productId);

        cartRepository.save(cart);
    }

    @Override
    public CartItemDto convertToDto(CartItem cartItem) {
        return modelMapper.map(cartItem, CartItemDto.class);
    }
}
