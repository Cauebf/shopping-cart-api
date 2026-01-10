package com.github.cauebf.shoppingcartapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.cauebf.shoppingcartapi.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> { // JpaRepository<Entity, primary key type>
    // JpaRepository has a lot of methods (save, delete, findById, findAll, etc.) and we can create our own methods
    // Jpa is creating the queries for this methods automatically because they are following the naming convention
    List<Product> findByCategoryName(String category);

    List<Product> findByBrand(String brand);

    List<Product> findByCategoryNameAndBrand(String category, String brand);

    List<Product> findByName(String name);

    List<Product> findByBrandAndName(String brand, String name);

    Long countByBrandAndName(String brand, String name); 
    
}
