package com.example.chopar_1.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileLoginRequestDTO {
    private String email;
    private String password;
    private String phone;
}
