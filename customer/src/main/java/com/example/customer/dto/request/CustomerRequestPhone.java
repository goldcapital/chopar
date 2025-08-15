package com.example.customer.dto.request;


import jakarta.validation.constraints.NotBlank;

public record CustomerRequestPhone(

        @NotBlank(message = "Customer firstname is required")
        String firstname,
        @NotBlank
        String phone,
        String lastname,

        @NotBlank
        String password
) {
}
