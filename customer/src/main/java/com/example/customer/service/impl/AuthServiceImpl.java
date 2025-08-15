package com.example.customer.service.impl;


import com.example.customer.dto.ProfileDTO;
import com.example.customer.dto.request.CustomerRequestPhone;
import com.example.customer.dto.request.KafkaSendMessage;
import com.example.customer.dto.request.ProfileLoginRequestDTO;
import com.example.customer.dto.response.SmsResponse;
import com.example.customer.entity.ProfileEntity;
import com.example.customer.enums.AppLanguage;
import com.example.customer.enums.ProfileStatus;
import com.example.customer.exp.AppBadException;
import com.example.customer.mapper.ProfileMapper;
import com.example.customer.repository.ProfileRepository;
import com.example.customer.request.CustomerRequest;
import com.example.customer.service.*;
import com.example.customer.service.kafka.KafkaProducer;
import com.example.customer.util.JWTUtil;
import com.example.customer.util.MDUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.example.customer.util.JWTUtil.decode;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final ProfileRepository profileRepository;
    private final ResourceBundleService resourceBundleService;
    private final NotificationClient notificationClient;
    private final KafkaProducer kafkaProducer;
    private final NotificationService notificationService;
    private final ProfileMapper profileMapper;
    private final KeycloakService keycloakService;

    @Override
    public SmsResponse registrationPhone(CustomerRequestPhone dto, AppLanguage appLanguage) {
        var keycloakId = keycloakService.creatKeycloakUser(
                new CustomerRequest(dto.firstname(), dto.phone(), dto.lastname(), null, dto.password()));
,
        if (getProfileCheck(dto, appLanguage)) {
            var entity = profileMapper.toEntityPhone(dto, MDUtil.encode(dto.password()), ProfileStatus.REGISTRATION);
            profileRepository.save(entity);
            kafkaProducer.sendPhone(dto.phone());
            return new SmsResponse("iltimos codni kiriting", 40);

        }
        throw new AppBadException(resourceBundleService.getMessage("", appLanguage));


    }

    @Override
    public ProfileDTO registrationEmail(ProfileDTO dto, AppLanguage appLanguage) {
        getProfileCheck(dto, appLanguage);
        String password = MDUtil.encode(dto.getPassword());
        var entity = profileMapper.toEntity(dto, password, ProfileStatus.REGISTRATION);
        var jwt = JWTUtil.encode(entity.getPhone(), entity.getEmail(), entity.getRole(), appLanguage);
        kafkaProducer.sendEmail(new KafkaSendMessage(entity.getEmail(), jwt, entity.getName()));
        profileRepository.save(entity);
        //emailga HABAR YUBORILDI MASSEG QAYTARISH KERAK
        return dto;
    }

    @Override
    public ProfileDTO loge(ProfileLoginRequestDTO dto, AppLanguage appLanguage) {
        return profileRepository.findByEmailOrPhone(dto.getEmail(), dto.getPhone())
                .filter(entity -> entity.getStatus().equals(ProfileStatus.ACTIVE))
                .filter(entity -> {
                    if (!Objects.equals(entity.getPassword(), MDUtil.encode(dto.getPassword()))) {
                        throw new AppBadException(resourceBundleService.getMessage("email.password.wrong", appLanguage));
                    }
                    return true;
                })
                .map(entity -> {
                    var profileDTO = new ProfileDTO();
                    profileDTO.setName(entity.getName());
                    profileDTO.setJwt(JWTUtil.encode(dto.getPhone(), dto.getEmail(), entity.getRole(), appLanguage));
                    return profileDTO;

                }).orElseThrow(() -> new AppBadException(resourceBundleService.getMessage("item.not.found", appLanguage)));


    }

    @Override
    public Boolean smsVerification(String phone, String code, AppLanguage appLanguage) {

        var profileEntity = profileRepository.findByPhone(phone).orElseThrow(() ->
                new AppBadException(resourceBundleService.getMessage("", appLanguage)));

        if (profileEntity.getStatus().equals(ProfileStatus.ACTIVE)) {
            throw new AppBadException(resourceBundleService.getMessage("This.phone.has.been.registered", appLanguage));
        }

        return notificationClient.getByPhoneCheck(phone, code, appLanguage);
    }

    @Override
    public Boolean emailVerification(String token) {
        var jwtDTO = decode(token);
        return profileRepository.getId(jwtDTO.getEmail())
                .map(entity -> {
                            if (Objects.equals(entity.getStatus(), ProfileStatus.ACTIVE)) {
                                throw new AppBadException(resourceBundleService.getMessage("This.email.has.been.registered", jwtDTO.getAppLanguage()));
                            }
                            profileRepository.update(ProfileStatus.ACTIVE, jwtDTO.getEmail());
                            return true;
                        }
                ).orElseThrow(() -> new AppBadException(resourceBundleService.getMessage("item.not.found", jwtDTO.getAppLanguage())));

    }

    private boolean getProfileCheck(ProfileDTO dto, AppLanguage language) {
        var identifier = dto.getEmail() != null ? dto.getEmail() : dto.getPhone() != null ? dto.getPhone() : "UNKNOWN";
        if (notificationService.isBlocked(identifier)) {
            throw new AppBadException(resourceBundleService.getMessage("blocked", language));
        }
/*        var optional = profileRepository.findByEmailOrPhone(dto.getEmail(), dto.getPhone());
        if (optional.isEmpty()) {
            return true;
        }

        if (optional.get().getStatus().equals(ProfileStatus.ACTIVE)
                || optional.get().getStatus().equals(ProfileStatus.NOT_ACTIVE)) {
            throw new AppBadException(resourceBundleService.getMessage("This.email.has.been.registered", language));
        }*/
        if (dto.getEmail() != null) {
            profileRepository.deleteByEmail(dto.getEmail());
        } else if (dto.getPhone() != null) {
            profileRepository.deleteByPhone(dto.getPhone());
        } else {
            //emal yoki phone hato mesag
            throw new AppBadException(resourceBundleService.getMessage("email.not.found", language));
        }
        notificationService
                .recordLoginAttempt(identifier);
        return true;
    }

    private boolean checkAttemptLimits(ProfileDTO dto, AppLanguage appLanguage) {
        var from = LocalDateTime.now().minusMinutes(1);
        var to = LocalDateTime.now();

        boolean tooManyAttempts = (dto.getEmail() != null && notificationClient.getCountSendEmail(dto.getEmail(), from, to) >= 3) ||
                (dto.getPhone() != null && notificationClient.getCountSendSms(dto.getPhone(), from, to) >= 3);
        if (tooManyAttempts) {
            throw new AppBadException(resourceBundleService.getMessage("To.many.attempt.Please.try.after.one.minute", appLanguage));
        }
        return true;
    }
}