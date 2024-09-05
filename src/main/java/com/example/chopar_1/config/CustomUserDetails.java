package com.example.chopar_1.config;

import com.example.chopar_1.enums.ProfileRole;
import com.example.chopar_1.enums.ProfileStatus;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    @Getter
    private String id;
    @Getter
    private String email;
    private String password;
    private ProfileStatus status;
    private ProfileRole role;



    public CustomUserDetails(String id, String email, String password, ProfileStatus status, ProfileRole role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.status = status;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new LinkedList<>();
        list.add(new SimpleGrantedAuthority(role.name())); // ROLE
        return list;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {//akovutni mudati tugamaganmi
        return true;//
    }

    @Override
    public boolean isAccountNonLocked() {//bloklamaganmi
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {//berilgan rolarni mudati utmaganmi
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status.equals(ProfileStatus.ACTIVE);//Active bulsa true kitadi
        //agar bironta metod folse yuborsa user uta olmaydi
    }

}
