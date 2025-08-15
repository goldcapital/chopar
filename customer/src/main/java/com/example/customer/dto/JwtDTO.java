package com.example.customer.dto;

import com.example.customer.enums.AppLanguage;
import com.example.customer.enums.ProfileRole;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtDTO {
    private String phone;
    private String email;
    private ProfileRole role;
    private AppLanguage appLanguage;

   /* public JwtDTO() {
    }

    public JwtDTO(String email, ProfileRole role, AppLanguage appLanguage) {
        this.email = email;
        this.role = role;
        this.appLanguage = appLanguage;
    }

    public JwtDTO(String phone , AppLanguage appLanguage, ProfileRole role) {
        this.role = role;
        this.appLanguage=appLanguage;
        this.phone=phone;

    }*/
}
