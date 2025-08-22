package com.example.customer.dto.request;

import lombok.Getter;
import lombok.Setter;


public record ProfileLoginRequestDTO(
        String email,
        String password,
        String phone
) {

}
