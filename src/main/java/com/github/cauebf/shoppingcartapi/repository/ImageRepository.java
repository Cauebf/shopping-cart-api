package com.github.cauebf.shoppingcartapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.cauebf.shoppingcartapi.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
    
}
