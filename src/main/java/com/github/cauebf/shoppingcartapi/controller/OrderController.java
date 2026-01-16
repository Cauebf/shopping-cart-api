package com.github.cauebf.shoppingcartapi.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.cauebf.shoppingcartapi.dto.OrderDto;
import com.github.cauebf.shoppingcartapi.exceptions.ResourceNotFoundException;
import com.github.cauebf.shoppingcartapi.model.User;
import com.github.cauebf.shoppingcartapi.response.ApiResponse;
import com.github.cauebf.shoppingcartapi.service.order.IOrderService;
import com.github.cauebf.shoppingcartapi.service.user.IUserService;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final IOrderService orderService;
    private final IUserService userService;

    public OrderController(IOrderService orderService, IUserService userService) {
        // construtor dependency injection
        this.orderService = orderService;
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId) {
        try {
            OrderDto order = orderService.getOrder(orderId);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Order found!", order));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getOrderByUserId() {
        try {
            User user = userService.getAuthenticatedUser();
            List<OrderDto> orders = orderService.getUserOrders(user.getId());
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Order found!", orders));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createOrder() {
        try {
            User user = userService.getAuthenticatedUser();
            OrderDto order = orderService.placeOrder(user.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Order created!", order));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
