package com.example.chopar_1.service;

import com.example.chopar_1.config.CustomUserDetails;
import com.example.chopar_1.config.CustomUserDetailsService;
import com.example.chopar_1.dto.JwtDTO;
import com.example.chopar_1.dto.ProfileDTO;
import com.example.chopar_1.entity.ProfileEntity;
import com.example.chopar_1.enums.AppLanguage;
import com.example.chopar_1.enums.ProfileRole;
import com.example.chopar_1.enums.ProfileStatus;
import com.example.chopar_1.exp.AppBadException;
import com.example.chopar_1.repository.ProfileRepository;
import com.example.chopar_1.util.JWTUtil;
import com.example.chopar_1.util.MDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class AuthService {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ResourceBundleService resourceBundleService;
    @Autowired
    private MailSenderService mailSenderService;

    public ProfileDTO registration(ProfileDTO dto, AppLanguage appLanguage) {

        if (dto.getPhone() != null) {
        }
        registrationEmail(dto, appLanguage);
        return dto;
    }

    public ProfileDTO registrationEmail(ProfileDTO dto, AppLanguage appLanguage) {
        Optional<ProfileEntity> optional = profileRepository.findByEmail(dto.getEmail());
        if (optional.isPresent()) {
            if (optional.get().getStatus().equals(ProfileStatus.REGISTRATION)) {
                profileRepository.deleteByEmail(optional.get().getEmail());
            }
            throw new AppBadException(resourceBundleService.getMessage("This.email.has.been.registered", appLanguage));
        }
        ProfileEntity entity = new ProfileEntity();

        entity.setRole(ProfileRole.ROLE_USER);
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setStatus(ProfileStatus.REGISTRATION);
        entity.setName(dto.getName());

        entity.setJwt(JWTUtil.encode(entity.getEmail(), entity.getRole(), appLanguage));
        profileRepository.save(entity);

        String text = "<h1 style=\"=text-align: center\">Hello %s</h1>\n" +
                "<p style=\"background-color: indianred; color: white; padding:30px\"> To complete registration please link to the following link </p>\n" +
                "<a style=\"background-color: #f44336;\n" +
                "  color: white;\n" +
                "  padding: 14px 25px;\n" +
                "  text-align: center;\n" +
                "  text-decoration: none;\n" +
                "  display: inline-block;\" href=\"http://localhost:8081/auth/verification/email/%s\n" +
                "\">Click</a>\n" +
                "<br>\n";

        text = String.format(text, dto.getName(), entity.getJwt());
        mailSenderService.sendEmail(entity.getEmail(), "Complete registration", text);


        return dto;
    }

    public ProfileEntity toEntity(ProfileDTO dto) {
        ProfileEntity entity = new ProfileEntity();

        entity.setRole(ProfileRole.ROLE_USER);
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setStatus(ProfileStatus.REGISTRATION);
        entity.setName(dto.getName());
        //  entity.setJwt(JWTUtil.encode(entity.getId(),entity.getRole()));
        return entity;
    }

    public Boolean emailVerification(String token) {

        JwtDTO jwtDTO = JWTUtil.decode(token);

        Optional<ProfileEntity> optional = profileRepository.getId(jwtDTO.getEmail());
        if (optional.isEmpty()) {
            throw new AppBadException(resourceBundleService.getMessage("item.not.found", jwtDTO.getAppLanguage()));
        }
      profileRepository.update(ProfileStatus.ACTIVE, jwtDTO.getEmail());

        return null;
    }

    public ProfileDTO loge(ProfileDTO dto, AppLanguage appLanguage) {
        Optional<ProfileEntity> optional = profileRepository.findByEmail(dto.getEmail());
        if (optional.isPresent()) {
            if (optional.get().getStatus().equals(ProfileStatus.ACTIVE) && optional.get().getPassword().equals(MDUtil.encode(dto.getPassword()))) {
                //  throw new AppBadException(resourceBundleService.getMessage("item.not.found",appLanguage));
                ProfileEntity entity = optional.get();
                ProfileDTO profileDTO = new ProfileDTO();
                profileDTO.setName(entity.getName());
                profileDTO.setJwt(JWTUtil.encode(dto.getEmail(), entity.getRole(), appLanguage));
                return profileDTO;
            }
            throw new AppBadException(resourceBundleService.getMessage("email.password.wrong",appLanguage));

        }
        throw new AppBadException(resourceBundleService.getMessage("item.not.found",appLanguage));
    }
   /* public RegionEntity get(Integer id) {
        return regionRepository.findById(id).orElseThrow(() -> {
            // warning
            throw new AppBadException("Region not found");
        });*/
}