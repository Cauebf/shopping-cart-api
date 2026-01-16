package com.github.cauebf.shoppingcartapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.cauebf.shoppingcartapi.dto.CartDto;
import com.github.cauebf.shoppingcartapi.exceptions.ResourceNotFoundException;
import com.github.cauebf.shoppingcartapi.model.Cart;
import com.github.cauebf.shoppingcartapi.model.User;
import com.github.cauebf.shoppingcartapi.response.ApiResponse;
import com.github.cauebf.shoppingcartapi.service.cart.ICartService;
import com.github.cauebf.shoppingcartapi.service.user.IUserService;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final ICartService cartService;
    private final IUserService userService;

    public CartController(ICartService cartService, IUserService userService) {
        // construtor dependency injection
        this.cartService = cartService;
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getCart() {
        try {
            User user = userService.getAuthenticatedUser();
            Cart cart = cartService.getCartByUserId(user.getId());
            CartDto cartDto = cartService.convertToDto(cart);

            return ResponseEntity.ok(new ApiResponse("Cart found!", cartDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCart(@PathVariable Long id) {
        try {
            cartService.deleteCart(id);
            return ResponseEntity.ok(new ApiResponse("Cart deleted!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
