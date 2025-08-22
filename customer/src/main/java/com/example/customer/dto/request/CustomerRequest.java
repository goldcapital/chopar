package com.example.customer.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CustomerRequest(
        @NotBlank(message = "Customer firstname is required")
        String firstname,
        String phone,
        String lastname,
        @Email
        String email,
        @NotBlank
        String password
) {
}
