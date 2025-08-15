package com.example.customer.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CustomerRequest(

        @NotBlank(message = "Customer firstname is required")
        String firstname,
        String phone,
        String lastname,
        @Email(message = "Customer email is not valid email address")
        String email,
        @NotBlank
        String password
       // Address address


) {
}
