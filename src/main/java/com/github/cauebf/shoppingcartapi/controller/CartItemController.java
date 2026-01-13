package com.github.cauebf.shoppingcartapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.cauebf.shoppingcartapi.dto.CartItemDto;
import com.github.cauebf.shoppingcartapi.exceptions.ResourceNotFoundException;
import com.github.cauebf.shoppingcartapi.model.Cart;
import com.github.cauebf.shoppingcartapi.model.CartItem;
import com.github.cauebf.shoppingcartapi.model.User;
import com.github.cauebf.shoppingcartapi.response.ApiResponse;
import com.github.cauebf.shoppingcartapi.service.cart.ICartItemService;
import com.github.cauebf.shoppingcartapi.service.cart.ICartService;
import com.github.cauebf.shoppingcartapi.service.user.IUserService;

@RestController
@RequestMapping("/carts/items")
public class CartItemController {
    private final ICartItemService cartItemService;
    private final ICartService cartService;
    private final IUserService userService;

    public CartItemController(ICartItemService cartItemService, ICartService cartService, IUserService userService) {
        // construtor dependency injection
        this.cartItemService = cartItemService;
        this.cartService = cartService;
        this.userService = userService;
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse> getCartItem(@RequestParam Long cartId, @PathVariable Long productId) {
        try {
            CartItem cartItem = cartItemService.getCartItem(cartId, productId);
            CartItemDto cartItemDto = cartItemService.convertToDto(cartItem);
            
            return ResponseEntity.ok(new ApiResponse("Cart item found!", cartItemDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @Transactional
    @PostMapping
    public ResponseEntity<ApiResponse> addItemToCart(@RequestParam Long productId, @RequestParam Integer quantity) {
        try {
            // get the user, if the user has no cart, create one
            User user = userService.getUserById(1L); // TODO: replace with the actual user id
            Cart cart = cartService.findOrCreateCartByUser(user);
            
            cartItemService.addItemToCart(cart.getId(), productId, quantity);
            return ResponseEntity.ok(new ApiResponse("Cart item added successfully!", null));
        } catch (ResourceNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse> updateItemQuantity(@RequestParam Long cartId, 
                                                      @PathVariable Long productId, 
                                                      @RequestParam Integer quantity) {
        try {
            cartItemService.updateItemQuantity(cartId, productId, quantity);
            return ResponseEntity.ok(new ApiResponse("Cart item updated successfully!", null));
        } catch (ResourceNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse> removeItemFromCart(@RequestParam Long cartId, @PathVariable Long productId) {
        try {
            cartItemService.removeItemFromCart(cartId, productId);
            return ResponseEntity.ok(new ApiResponse("Cart item removed successfully!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
