package com.github.cauebf.shoppingcartapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.cauebf.shoppingcartapi.model.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    // Jpa creates the query automatically based on the method name
    List<Image> findByProductId(Long id);
}
