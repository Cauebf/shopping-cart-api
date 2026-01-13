package com.github.cauebf.shoppingcartapi.service.order;

import java.util.List;

import com.github.cauebf.shoppingcartapi.dto.OrderDto;

public interface IOrderService {
    OrderDto placeOrder(Long userId);
    OrderDto getOrder(Long orderId);
    List<OrderDto> getUserOrders(Long userId);
}
