package com.github.cauebf.shoppingcartapi.service.product;

import java.util.List;

import com.github.cauebf.shoppingcartapi.model.Product;
import com.github.cauebf.shoppingcartapi.request.AddProductRequest;
import com.github.cauebf.shoppingcartapi.request.ProductUpdateRequest;

public interface IProductService {
    List<Product> getProducts(String name, String brand, String category);
    Product getProductById(Long id);
    Product addProduct(AddProductRequest request);
    void deleteProductById(Long id);
    Product updateProduct(ProductUpdateRequest product, Long productId);
    Long countProducts(String name, String brand, String category);
}
