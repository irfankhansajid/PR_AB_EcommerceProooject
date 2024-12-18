package com.example.pr_ab_ecommerceproooject.service;

import com.example.pr_ab_ecommerceproooject.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> getAllProduct();
    Optional<Product> getProductById(Long id) throws Exception;
    Product getProductByName(String productName);

    Product createProduct(Product product);
    boolean deleteProduct(Long id);

}
