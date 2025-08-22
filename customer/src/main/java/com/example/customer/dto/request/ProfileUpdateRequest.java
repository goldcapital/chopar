package com.example.customer.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record ProfileUpdateRequest(

        String name,

        @Pattern(regexp = "^\\+[0-9]{10,15}$")
        String phone,

        @Email
        String email,

        Boolean activated,

        @JsonIgnore
        String password
) {
}
