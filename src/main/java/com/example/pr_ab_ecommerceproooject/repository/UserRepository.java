package com.example.pr_ab_ecommerceproooject.repository;

import com.example.pr_ab_ecommerceproooject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
}
