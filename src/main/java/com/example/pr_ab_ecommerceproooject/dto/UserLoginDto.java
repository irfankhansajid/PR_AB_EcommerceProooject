package com.example.pr_ab_ecommerceproooject.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginDto {

    @NotBlank(message = "Username or email is required")
    private String email; // You can also use email here

    @NotBlank(message = "Password is required")
    private String password;
}
