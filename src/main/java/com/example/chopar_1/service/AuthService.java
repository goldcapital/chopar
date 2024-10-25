package com.example.chopar_1.service;

import com.example.chopar_1.dto.JwtDTO;
import com.example.chopar_1.dto.ProfileDTO;
import com.example.chopar_1.dto.request.ProfileLoginRequestDTO;
import com.example.chopar_1.entity.EmailHistoryEntity;
import com.example.chopar_1.entity.ProfileEntity;
import com.example.chopar_1.entity.SmsHistoryEntity;
import com.example.chopar_1.enums.AppLanguage;
import com.example.chopar_1.enums.ProfileRole;
import com.example.chopar_1.enums.ProfileStatus;
import com.example.chopar_1.exp.AppBadException;
import com.example.chopar_1.repository.EmailHistoryRepository;
import com.example.chopar_1.repository.ProfileRepository;
import com.example.chopar_1.util.JWTUtil;
import com.example.chopar_1.util.MDUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.chopar_1.util.JWTUtil.decode;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final ProfileRepository profileRepository;
    private final ResourceBundleService resourceBundleService;
    private final MailSenderService mailSenderService;
    private final EmailHistoryRepository emailHistoryRepository;
    private final SmsHistoryService smsHistoryService;


    public SmsHistoryEntity registrationPhone(ProfileDTO dto, AppLanguage appLanguage) {

        if (getProfileCheck(dto, appLanguage)) {

            var profileEntity = new ProfileEntity();

            profileEntity.setName(dto.getName());
            profileEntity.setPassword(MDUtil.encode(dto.getPassword()));
            profileEntity.setRole(ProfileRole.ROLE_USER);
            profileEntity.setPhone(dto.getPhone());
            profileEntity.setStatus(ProfileStatus.REGISTRATION);
            profileRepository.save(profileEntity);

         return smsHistoryService.crate(dto.getPhone());
        }
        throw new AppBadException(resourceBundleService.getMessage("", appLanguage));
    }

    public ProfileDTO registrationEmail(ProfileDTO dto, AppLanguage appLanguage) {
        getProfileCheck(dto, appLanguage);


        ProfileEntity entity = new ProfileEntity();

        entity.setRole(ProfileRole.ROLE_USER);
        entity.setEmail(dto.getEmail());
        entity.setPassword(MDUtil.encode(dto.getPassword()));
        entity.setStatus(ProfileStatus.REGISTRATION);
        entity.setName(dto.getName());

        entity.setJwt(JWTUtil.encode(entity.getPhone(), entity.getEmail(), entity.getRole(), appLanguage));
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
        EmailHistoryEntity emailHistoryEntity = new EmailHistoryEntity();
        emailHistoryEntity.setEmail(dto.getEmail());
        emailHistoryEntity.setMessage(text);
        emailHistoryRepository.save(emailHistoryEntity);
        return dto;
    }

    public Boolean smsVerification(String phone, String code, AppLanguage appLanguage) {

        ProfileEntity profileEntity= profileRepository.findByPhone(phone).orElseThrow(()->
                new AppBadException(resourceBundleService.getMessage("",appLanguage)));

        if (profileEntity.getStatus().equals(ProfileStatus.ACTIVE)) {
            throw new AppBadException(resourceBundleService.getMessage("This.phone.has.been.registered", appLanguage));
        }

       return smsHistoryService.getByPhoneCheck(phone,code,appLanguage);
    }


    public Boolean emailVerification(String token) {
        JwtDTO jwtDTO = decode(token);
        Optional<ProfileEntity> optional = profileRepository.getId(jwtDTO.getEmail());

        if (optional.isEmpty()) {
            throw new AppBadException(resourceBundleService.getMessage("item.not.found", jwtDTO.getAppLanguage()));
        }
        if (!optional.get().getStatus().equals(ProfileStatus.ACTIVE)) {
            resourceBundleService.getMessage("please.tyre.again", jwtDTO.getAppLanguage());

            profileRepository.update(ProfileStatus.ACTIVE, jwtDTO.getEmail());
        }
        throw new AppBadException(resourceBundleService.getMessage("This.email.has.been.registered", jwtDTO.getAppLanguage()));

    }

    private Boolean getProfileCheck(ProfileDTO dto, AppLanguage language) {

        Optional<ProfileEntity> optional = profileRepository.findByEmailOrPhone(dto.getEmail(), dto.getPhone());

        if (optional.isEmpty()) {
            return true;
        }

        if (optional.get().getStatus().equals(ProfileStatus.ACTIVE)
                || optional.get().getStatus().equals(ProfileStatus.NOT_ACTIVE)) {
            throw new AppBadException(resourceBundleService.getMessage("This.email.has.been.registered", language));
        }

        profileRepository.deleteByEmail(optional.get().getEmail());

        LocalDateTime from = LocalDateTime.now().minusMinutes(1);
        LocalDateTime to = LocalDateTime.now();

        if (dto.getEmail() != null && emailHistoryRepository.countSendEmail(dto.getEmail(), from, to) >= 3 ||
                dto.getPhone() != null && smsHistoryService.getCountSendSms(dto.getPhone(), from, to) >= 3) {
            throw new AppBadException(resourceBundleService.getMessage("To.many.attempt.Please.try.after.one.minute", language));
        }
        return true;
    }

    public ProfileDTO loge(ProfileLoginRequestDTO dto, AppLanguage appLanguage) {
        ProfileEntity profileEntity = profileRepository.findByEmailOrPhone(dto.getEmail(), dto.getPhone())
                .orElseThrow(() -> new AppBadException(resourceBundleService.getMessage("item.not.found", appLanguage)));


        if (profileEntity.getStatus().equals(ProfileStatus.ACTIVE)
                && profileEntity.getPassword().
                equals(MDUtil.encode(dto.getPassword()))) {


            ProfileDTO profileDTO = new ProfileDTO();
            profileDTO.setName(profileEntity.getName());
            profileDTO.setJwt(JWTUtil.encode(dto.getPhone(), dto.getEmail(), profileEntity.getRole(), appLanguage));
            return profileDTO;
        }
        throw new AppBadException(resourceBundleService.getMessage("email.password.wrong", appLanguage));

    }
}