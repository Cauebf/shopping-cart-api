package com.github.cauebf.shoppingcartapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.cauebf.shoppingcartapi.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

} 
