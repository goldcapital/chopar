package com.example.customer.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileLoginRequestDTO {
    private String email;
    private String password;
    private String phone;
}
