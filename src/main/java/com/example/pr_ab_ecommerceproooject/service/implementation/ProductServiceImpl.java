package com.example.pr_ab_ecommerceproooject.service.implementation;

import com.example.pr_ab_ecommerceproooject.exception.AlreadyExistsException;
import com.example.pr_ab_ecommerceproooject.exception.NotFoundException;
import com.example.pr_ab_ecommerceproooject.model.Product;
import com.example.pr_ab_ecommerceproooject.repository.ProductRepository;
import com.example.pr_ab_ecommerceproooject.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getProductById(Long id){
        log.info("Fetching product with id: {}", id);
        if (id == null) {
            log.error("Id cannot be null");
            throw new NotFoundException("Product not found with id");
        }
        return productRepository.findById(id);
    }

    @Override
    public Product getProductByName(String productName) {
        log.info("Fetching product with name: {}", productName);
        if (productName == null || productName.isEmpty()) {
            log.error("Product name cannot be null or empty");
            throw new IllegalArgumentException("Product cannot be null or empty");
        }
        return productRepository.findByName(productName);
    }

    @Override
    public Product createProduct(Product product) {

        Optional<Product> isExits = productRepository.findById(product.getId());
        if (isExits.isPresent()) {
            throw new AlreadyExistsException("Product already exits with id");
        }
        Product newProduct = new Product();
        newProduct.setName(product.getName());
        newProduct.setDescription(product.getDescription());
        newProduct.setPrice(product.getPrice());
        newProduct.setImageUrl(product.getImageUrl());


        return productRepository.save(newProduct);


    }

    @Override
    public boolean deleteProduct(Long id) {
        log.info("Deleting product with id: {}", id);
        if (!productRepository.existsById(id)) {
            log.warn("Attempted to delete non-existent product with id: {}", id);
            return false;
        }
        productRepository.deleteById(id);
        log.info("Delete product with id successfully: {}", id);
        return true;
    }
}
