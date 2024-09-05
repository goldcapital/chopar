package com.example.chopar_1.util;

import com.example.chopar_1.config.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SpringSecurityUtil {


    public static CustomUserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String currentPrincipalName = authentication.getName(); // username
        //Principal security dagi user ni malumotlarini olish
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails;
    }
}
