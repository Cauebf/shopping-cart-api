package com.github.cauebf.shoppingcartapi.service.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.cauebf.shoppingcartapi.dto.OrderDto;
import com.github.cauebf.shoppingcartapi.enums.OrderStatus;
import com.github.cauebf.shoppingcartapi.exceptions.ResourceNotFoundException;
import com.github.cauebf.shoppingcartapi.model.Cart;
import com.github.cauebf.shoppingcartapi.model.Order;
import com.github.cauebf.shoppingcartapi.model.OrderItem;
import com.github.cauebf.shoppingcartapi.model.Product;
import com.github.cauebf.shoppingcartapi.repository.OrderRepository;
import com.github.cauebf.shoppingcartapi.repository.ProductRepository;
import com.github.cauebf.shoppingcartapi.service.cart.ICartService;

@Service
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ICartService cartService;
    private final ModelMapper modelMapper;

    public OrderService(
        OrderRepository orderRepository, 
        ProductRepository productRepository, 
        ICartService cartService, 
        ModelMapper modelMapper
    ) {
        // construtor dependency injection
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.cartService = cartService;
        this.modelMapper = modelMapper;
    }

    @Transactional // if something goes wrong, the transaction will be rolled back
    @Override
    public OrderDto placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);

        Order order = createOrder(cart);
        List<OrderItem> orderItems = createOrderItems(order, cart);

        order.setOrderItems(new HashSet<>(orderItems));
        order.setTotalAmount(calculateTotalAmount(orderItems));
        Order savedOrder = orderRepository.save(order); // save the order to the db

        cartService.clearCart(cart.getId()); // clear the cart
        return convertToDto(savedOrder);
    }

    private Order createOrder(Cart cart) {
        Order order = new Order();

        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        
        return order;
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart) {
        // iterate through the cart items and create order items
        return cart.getItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();

            // decrease the inventory of the product by the quantity of the cart item
            product.setInventory(product.getInventory() - cartItem.getQuantity());
            productRepository.save(product);

            // return the order item
            return new OrderItem(
                product, 
                order, 
                cartItem.getQuantity(), 
                cartItem.getUnitPrice()
            );
        }).toList(); // return the list of order items
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList) {
        return orderItemList.stream()
                .map(orderItem -> orderItem.getPrice()
                        .multiply(new BigDecimal(orderItem.getQuantity()))) // multiply the price by the quantity
                .reduce(BigDecimal.ZERO, BigDecimal::add); // sum the total prices starting from zero
    }

    @Override
    public OrderDto getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found!"));
        return convertToDto(order);
    }
    
    @Override
    public List<OrderDto> getUserOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(this::convertToDto).toList();
    }

    private OrderDto convertToDto(Order order) {
        return modelMapper.map(order, OrderDto.class);
    }
}
