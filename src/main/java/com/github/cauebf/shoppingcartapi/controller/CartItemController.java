package com.github.cauebf.shoppingcartapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.github.cauebf.shoppingcartapi.model.CartItem;
import com.github.cauebf.shoppingcartapi.response.ApiResponse;
import com.github.cauebf.shoppingcartapi.service.cart.ICartItemService;

@RestController
@RequestMapping("/carts/{cartId}/items")
public class CartItemController {
    private final ICartItemService cartItemService;

    public CartItemController(ICartItemService cartItemService) {
        // construtor dependency injection
        this.cartItemService = cartItemService;
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse> getCartItem(@PathVariable Long cartId, @PathVariable Long productId) {
        try {
            CartItem cartItem = cartItemService.getCartItem(cartId, productId);
            CartItemDto cartItemDto = cartItemService.convertToDto(cartItem);
            
            return ResponseEntity.ok(new ApiResponse("Cart item found!", cartItemDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addCartItem(@PathVariable Long cartId, 
                                                   @RequestParam Long productId, 
                                                   @RequestParam Integer quantity) {
        try {
            cartItemService.addCartItem(cartId, productId, quantity);
            return ResponseEntity.ok(new ApiResponse("Cart item added successfully!", null));
        } catch (ResourceNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping
    public ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable Long cartId, 
                                                      @RequestParam Long productId, 
                                                      @RequestParam Integer quantity) {
        try {
            cartItemService.updateItemQuantity(cartId, productId, quantity);
            return ResponseEntity.ok(new ApiResponse("Cart item updated successfully!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId, @PathVariable Long productId) {
        try {
            cartItemService.removeItemFromCart(cartId, productId);
            return ResponseEntity.ok(new ApiResponse("Cart item removed successfully!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
