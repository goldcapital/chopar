package com.example.chopar_1.dto;

import com.example.chopar_1.enums.AppLanguage;
import com.example.chopar_1.enums.ProfileRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JwtDTO {
    private String email;
    private ProfileRole role;
    private AppLanguage appLanguage;

}
