package com.example.chopar_1.config;


import com.example.chopar_1.entity.ProfileEntity;
import com.example.chopar_1.exp.AppBadException;
import com.example.chopar_1.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private ProfileRepository profileRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Bu metodda biz spring securitga murojat
        // qilsak spring securit shu metoda  username olib kiladi bu email yoki phone bulishi mumkin
        // login/phone/email
        Optional<ProfileEntity> optional = profileRepository.findByEmailOrPhone(username,username);
        if (optional.isEmpty()) {
            throw new AppBadException("Bad Credentials. Mazgi");
        }

        ProfileEntity profile = optional.get();
        return new CustomUserDetails(profile.getId(), profile.getEmail(), profile.getPhone(),
                profile.getPassword(), profile.getStatus(), profile.getRole());
    }
}
