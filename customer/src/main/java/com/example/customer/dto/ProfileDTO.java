package com.example.customer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDTO {
    private Integer uuid;
    private String name;
    private String phone;
    private String email;
    private String password;
    private String jwt;

    public static ProfileDTO of(String name, String jwt) {
        return ProfileDTO.builder().name(name).jwt(jwt).build();
    }
}
