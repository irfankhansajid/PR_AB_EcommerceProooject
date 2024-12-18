package com.example.pr_ab_ecommerceproooject.repository;

import com.example.pr_ab_ecommerceproooject.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findByName(String productName);
}
