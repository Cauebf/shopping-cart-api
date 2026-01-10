package com.github.cauebf.shoppingcartapi.service.product;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.github.cauebf.shoppingcartapi.model.Product;
import com.github.cauebf.shoppingcartapi.exceptions.ResourceNotFoundException;
import com.github.cauebf.shoppingcartapi.model.Category;
import com.github.cauebf.shoppingcartapi.repository.CategoryRepository;
import com.github.cauebf.shoppingcartapi.repository.ProductRepository;
import com.github.cauebf.shoppingcartapi.request.AddProductRequest;
import com.github.cauebf.shoppingcartapi.request.ProductUpdateRequest;

@Service
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository CategoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository CategoryRepository) {
        // construtor dependency injection
        this.productRepository = productRepository; 
        this.CategoryRepository = CategoryRepository;
    }

    @Override
    public Product addProduct(AddProductRequest request) {
        // check if the category is found in the DB
        // if yes, set it as the new product category, if not, save it as a new category
        Category category = Optional.ofNullable(CategoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    // if not found, create a new category
                    Category newCategory = new Category(request.getCategory().getName()); 
                    return CategoryRepository.save(newCategory); // save the new category to the DB
                });
        request.setCategory(category); 
        return productRepository.save(createProduct(request, category)); // save the new product to the DB
    }

    private Product createProduct(AddProductRequest request, Category category) {
        return new Product(
            request.getName(),
            request.getBrand(),
            request.getPrice(),
            request.getInventory(),
            request.getDescription(),
            category
        );
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete, // if found, delete
                    () -> {throw new ResourceNotFoundException("Product not found!");});; // if not found, throw exception
    }

    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct, request)) // if found, update
                .map(productRepository::save) // save the updated product to the DB
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!")); // if not found, throw exception
    }

    private Product updateExistingProduct(Product existingProduct, ProductUpdateRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());
        
        Category category = CategoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }
    
}
