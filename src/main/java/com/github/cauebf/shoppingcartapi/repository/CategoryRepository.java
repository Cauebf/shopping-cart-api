package com.github.cauebf.shoppingcartapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.cauebf.shoppingcartapi.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Jpa creates the query automatically based on the method name
    Category findByName(String name);

    boolean existsByName(String name);
} 
