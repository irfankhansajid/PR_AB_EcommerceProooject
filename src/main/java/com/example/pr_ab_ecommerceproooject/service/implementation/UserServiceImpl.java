package com.example.pr_ab_ecommerceproooject.service.implementation;

import com.example.pr_ab_ecommerceproooject.exception.AlreadyExistsException;
import com.example.pr_ab_ecommerceproooject.exception.NotFoundException;
import com.example.pr_ab_ecommerceproooject.jwt.JwtUtils;
import com.example.pr_ab_ecommerceproooject.model.Role;
import com.example.pr_ab_ecommerceproooject.model.User;
import com.example.pr_ab_ecommerceproooject.repository.UserRepository;
import com.example.pr_ab_ecommerceproooject.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Transactional
    @Override
    public User registerUser(User user) {
        log.info("Registering user with username: {}", user.getUsername());

        if (userRepository.findByUsername(user.getUsername()).isPresent() || userRepository.findByEmail(user.getEmail()).isPresent()) {
            log.warn("User already exists with username: {} or email: {}", user.getUsername(), user.getEmail());
            throw new AlreadyExistsException("User is already registered");
        }
        User newUser = createUser(user);
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(newUser);
    }

    private User createUser(User user) {
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setFirstname(user.getFirstname());
        newUser.setLastname(user.getLastname());
        newUser.setPhoneNumber(user.getPhoneNumber());
        newUser.setRole(List.of(Role.USER));
        newUser.setActive(true);
        return newUser;
    }

    @Override
    public Optional<User> getUserById(Long id) {
        log.info("Fetching user with id: {}", id);
        if (id == null) {
            log.error("Id cannot be null");
            throw new IllegalArgumentException("Id cannot be null");
        }
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        log.info("Fetching user with email: {}", email);
        if (email == null || email.isEmpty()) {
            log.error("Email cannot be null or empty");
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        return Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        log.info("Fetching user with username: {}", username);
        if (username == null || username.isEmpty()) {
            log.error("Username cannot be null or empty");
            throw new NotFoundException("Username cannot be null or empty");
        }
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            log.error("User not found with username: {}", username);
            throw new NotFoundException("User not found with username: " + username);
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Fetching all users");
        return userRepository.findAll();
    }

    @Override
    public User updateUser(User user, Long id) {
        log.info("Updating user with id: {}", user.getId());
        User existUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new NotFoundException("User not found with id: " + user.getId()));
        if (user.getEmail() != null) {
            existUser.setEmail(user.getEmail());
        }
        if (user.getFirstname() != null) {
            existUser.setFirstname(user.getFirstname());
        }
        if (user.getLastname() != null) {
            existUser.setLastname(user.getLastname());
        }
        if (user.getPhoneNumber() != null) {
            existUser.setPhoneNumber(user.getPhoneNumber());
        }
        return userRepository.save(existUser);
    }

    @Override
    public boolean deleteUser(Long id) {
        log.info("Deleting user with id: {}", id);
        if (!userRepository.existsById(id)) {
            log.warn("Attempted to delete non-existent user with id: {}", id);
            return false;
        }
        userRepository.deleteById(id);
        log.info("Delete user with id successfully: {}", id);
        return true;
    }

    @Override
    public void changePassword(Long id, String oldPassword, String newPassword) {
        log.info("Changing password for user with id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            log.warn("Old password does not match for user with id: {}", id);
            throw new IllegalArgumentException("Old password is incorrect.");
        }
        if (!isValidNewPassword(newPassword)) {
            log.error("New password does not meet complexity requirements for user with id: {}", id);
            throw new IllegalArgumentException("New password does not meet complexity requirements.");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private boolean isValidNewPassword(String newPassword) {
        if (newPassword.length() < 8) return false;
        if (!newPassword.matches(".*[A-Z].*")) return false;
        if (!newPassword.matches(".*[a-z].*")) return false;
        if (!newPassword.matches(".*\\d.*")) return false;
        if (!newPassword.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) return false;
        return true;
    }

    @Override
    public String authenticateUser(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtUtils.generateToken(username);
    }

}