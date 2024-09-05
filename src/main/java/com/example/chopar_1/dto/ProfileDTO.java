package com.example.chopar_1.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDTO {
    private Integer uuid;//(uuid),
    private String  name;
    private  String phone;
    private String  email;
    private  String password;
    private String jwt;
    //status,role,visible,created_date, birthDate(not null)

}
