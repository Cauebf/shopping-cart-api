package com.github.cauebf.shoppingcartapi.service.cart;

import java.util.Optional;

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
        return findItem(cart, productId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found!")); 
    }

    @Override
     public void addCartItem(Long cartId, Long productId, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");

        Cart cart = cartService.getCart(cartId);
        Optional<CartItem> existingItem = findItem(cart, productId);

        CartItem item;
        if (existingItem.isPresent()) {
            // if the product is already in the cart, increase the quantity
            item = existingItem.get();
            item.increaseQuantity(quantity);
        } else {
            // if the product is not in the cart, create a new cart item and add it
            Product product = productService.getProductById(productId);
            item = new CartItem(product, cart);
            item.increaseQuantity(quantity);
            cart.getItems().add(item);
        }

        cart.recalculateTotal();
        cartRepository.save(cart);
    }

    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        CartItem item = findItem(cart, productId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found!"));
        
        if (quantity <= 0) {
            // if the quantity is 0 or less, remove the item from the cart
            cart.getItems().remove(item);
        } else {
            // if the quantity is greater than 0, update the quantity
            item.setQuantity(quantity);
        }

        cart.recalculateTotal();
        cartRepository.save(cart);
    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);
        CartItem item = findItem(cart, productId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found!"));

        cart.getItems().remove(item); // remove the item from the cart
        cart.recalculateTotal();
        cartRepository.save(cart);
    }

    private Optional<CartItem> findItem(Cart cart, Long productId) {
        return cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId)) // check if the product exists in the cart
                .findFirst();
    }

    @Override
    public CartItemDto convertToDto(CartItem cartItem) {
        return modelMapper.map(cartItem, CartItemDto.class);
    }
}
