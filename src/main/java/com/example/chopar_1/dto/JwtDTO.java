package com.example.chopar_1.dto;

import com.example.chopar_1.enums.AppLanguage;
import com.example.chopar_1.enums.ProfileRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class JwtDTO {
    private String phone;
    private String email;
    private ProfileRole role;
    private AppLanguage appLanguage;

    public JwtDTO(String email, ProfileRole role, AppLanguage appLanguage) {
        this.email = email;
        this.role = role;
        this.appLanguage = appLanguage;
    }

    public JwtDTO(String phone , AppLanguage appLanguage, ProfileRole role) {
        this.role = role;
        this.appLanguage=appLanguage;
        this.phone=phone;

    }
}
