package com.example.pr_ab_ecommerceproooject.controller;

import com.example.pr_ab_ecommerceproooject.exception.UserNotFoundException;
import com.example.pr_ab_ecommerceproooject.model.User;
import com.example.pr_ab_ecommerceproooject.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        if (userService.getAllUsers().isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(userService.getAllUsers());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        if (userService.getUserById(id).isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(userService.getUserById(id).get());
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        if (userService.getUserByEmail(email) == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(userService.getUserByEmail(email), HttpStatus.OK);
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        if (userService.getUserByUsername(username) == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(userService.getUserByUsername(username), HttpStatus.OK);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        try {
            return new ResponseEntity<>(userService.registerUser(user), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable Long id) {
        try {
            User updatedUser = userService.updateUser(user, id);
            return ResponseEntity.ok(updatedUser); // Return updated user with 200 OK
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build(); // Return 404 if user not found
        } catch (Exception e) {
            return ResponseEntity.badRequest().build(); // Return 400 for other errors
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Long id) {
        if (userService.deleteUser(id)) {
            log.info("Deleted user with id: {}", id);
            return ResponseEntity.ok(true);
        } else {
            log.warn("Attempted to delete non-existent user with id: {}", id);
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("/change-password/{id}")
    public ResponseEntity<String> changePassword(@PathVariable Long id,
                                                 @RequestParam String oldPassword,
                                                 @RequestParam String newPassword) {
        try {
            if (oldPassword == null || oldPassword.isEmpty() || newPassword == null || newPassword.isEmpty()) {
//                throw new IllegalArgumentException("Old password and new password cannot be null or empty");
                return ResponseEntity.badRequest().body("Error: Old password and new password cannot be null or empty"); // 400 Bad Request with error message

            }
            userService.changePassword(id, oldPassword, newPassword);
            return ResponseEntity.ok("Password changed successfully."); // 200 OK with success message
        } catch (IllegalArgumentException e) {
            log.error("Error changing password for user with id {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage()); // 400 Bad Request with error message
        } catch (UserNotFoundException e) {
            log.error("User not found: {}", e.getMessage());
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }


}
