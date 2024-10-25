package com.example.chopar_1.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDTO {
    private Integer uuid;
    private String  name;
    private  String phone;
    private String  email;
    private  String password;
    private String jwt;
}
