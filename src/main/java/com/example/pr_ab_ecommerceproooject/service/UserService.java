package com.example.pr_ab_ecommerceproooject.service;

import com.example.pr_ab_ecommerceproooject.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User registerUser(User user) throws Exception;
    Optional<User> getUserById(Long id);
    User getUserByEmail(String email);
    User getUserByUsername(String username);

    List<User> getAllUsers();

    User updateUser(User user);
    boolean deleteUser(Long id);
    boolean changePassword(Long id, String oldPassword, String newPassword);

}
