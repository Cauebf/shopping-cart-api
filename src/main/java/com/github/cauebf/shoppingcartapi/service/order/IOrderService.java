package com.github.cauebf.shoppingcartapi.service.order;

import java.util.List;

import com.github.cauebf.shoppingcartapi.model.Order;

public interface IOrderService {
    Order placeOrder(Long userId);
    Order getOrder(Long orderId);
    List<Order> getUserOrders(Long userId);
}
