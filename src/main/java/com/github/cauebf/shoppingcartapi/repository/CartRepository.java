package com.github.cauebf.shoppingcartapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.cauebf.shoppingcartapi.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

} 
