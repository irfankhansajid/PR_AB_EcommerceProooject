package com.example.pr_ab_ecommerceproooject.service;

import com.example.pr_ab_ecommerceproooject.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User registerUser(User user) throws Exception;
    Optional<User> getUserById(Long id);
    Optional<User> getUserByEmail(String email);
    Optional<User> getUserByUsername(String username);

    List<User> getAllUsers();

    User updateUser(User user, Long id);
    boolean deleteUser(Long id);
    void changePassword(Long id, String oldPassword, String newPassword);
    String authenticateUser(String username, String password);

}
