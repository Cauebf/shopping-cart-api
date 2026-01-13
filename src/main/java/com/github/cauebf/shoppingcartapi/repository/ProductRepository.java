package com.github.cauebf.shoppingcartapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.github.cauebf.shoppingcartapi.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> { // JpaRepository<Entity, primary key type>
    // JpaRepository has a lot of methods (save, delete, findById, findAll, etc.) and we can create our own methods
    boolean existsByNameAndBrand(String name, String brand);
    
    @Query("SELECT p FROM Product p " +
           "WHERE (:name IS NULL OR p.name = :name) " +
           "AND (:brand IS NULL OR p.brand = :brand) " +
           "AND (:category IS NULL OR p.category.name = :category)")
    List<Product> findProductsByOptionalFilters(
            @Param("name") String name,
            @Param("brand") String brand,
            @Param("category") String category
    );
    
    @Query("SELECT COUNT(p) FROM Product p " +
           "WHERE (:name IS NULL OR p.name = :name) " +
           "AND (:brand IS NULL OR p.brand = :brand) " +
           "AND (:category IS NULL OR p.category.name = :category)")
    Long countProductsByOptionalFilters(
            @Param("name") String name,
            @Param("brand") String brand,
            @Param("category") String category
    );
}
