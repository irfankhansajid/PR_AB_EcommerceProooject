package com.example.pr_ab_ecommerceproooject.controller;

import com.example.pr_ab_ecommerceproooject.dto.UserLoginDto;
import com.example.pr_ab_ecommerceproooject.dto.UserRegistrationDto;
import com.example.pr_ab_ecommerceproooject.exception.AlreadyExistsException;
import com.example.pr_ab_ecommerceproooject.model.User;
import com.example.pr_ab_ecommerceproooject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Validated @RequestBody UserRegistrationDto registrationDto) {
        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(registrationDto.getPassword());
        user.setFirstname(registrationDto.getFirstname()); // Set firstname
        user.setLastname(registrationDto.getLastname());
        user.setPhoneNumber(registrationDto.getPhoneNumber());
        try {
            return new ResponseEntity<>(userService.registerUser(user), HttpStatus.CREATED);
        }
        catch (AlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null); // 409 Conflict
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Validated @RequestBody UserLoginDto loginDto) {
        try {
            String jwt = userService.authenticateUser(loginDto.getEmail(), loginDto.getPassword());
            return ResponseEntity.ok(jwt);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

}
