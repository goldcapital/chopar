package com.example.customer.service.impl;


import com.example.customer.dto.request.CustomerRequest;
import com.example.customer.dto.request.KafkaSendMessage;
import com.example.customer.dto.request.ProfileLoginRequestDTO;
import com.example.customer.dto.response.AuthTokenResponse;
import com.example.customer.entity.ProfileEntity;
import com.example.customer.enums.AppLanguage;
import com.example.customer.enums.ProfileRole;
import com.example.customer.enums.ProfileStatus;
import com.example.customer.enums.SmsStatus;
import com.example.customer.exp.AppBadException;
import com.example.customer.mapper.ProfileMapper;
import com.example.customer.repository.ProfileRepository;
import com.example.customer.service.*;
import com.example.customer.service.kafka.KafkaProducer;
import com.example.customer.util.JWTUtil;
import com.example.customer.util.MDUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.example.customer.config.ThrowIfMessage.*;
import static com.example.customer.util.ConversionUtils.getUsername;
import static com.example.customer.util.JWTUtil.decode;


@Slf4j
@Service
@EnableCaching
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final ProfileRepository profileRepository;
    private final ResourceBundleService resourceBundleService;
    private final KafkaProducer kafkaProducer;
    private final NotificationService notificationService;
    private final ProfileMapper profileMapper;
    private final KeycloakService keycloakService;
    private final SmsServerService smsServerService;

    @Override
    public Boolean registration(CustomerRequest dto, AppLanguage appLanguage) {

        var identifier = getUsername(dto.email(), dto.phone());
        log.info("register user username is {}", identifier);
        var att = dto.phone() != null ? validateAndCleanProfile(identifier, profileRepository::findByPhone, profileRepository::deleteByPhone) :
                validateAndCleanProfile(identifier, profileRepository::findByEmail, profileRepository::deleteByEmail);

        var check = getProfileCheck(identifier, appLanguage);

        var password = MDUtil.encode(dto.password());
        var keycloakId = keycloakService.creatKeycloakUser(dto);

        var entity = profileMapper.toEntityPhone(identifier, dto, password, ProfileStatus.REGISTRATION, ProfileRole.ROLE_USER, keycloakId);

        var jwt = JWTUtil.encode(entity.getPhone(), entity.getEmail(), entity.getStatus(), appLanguage);

        if (check && dto.phone() != null) {
            kafkaProducer.sendPhone(dto.phone());
        } else {
            kafkaProducer.sendEmail(new KafkaSendMessage(entity.getEmail(), jwt, entity.getName()));
        }
        profileRepository.save(entity);
        return true;
    }

    private Boolean validateAndCleanProfile(String identifier,
                                            Function<String, Optional<ProfileEntity>> finder,
                                            Consumer<String> cleaner) {

        finder.apply(identifier).ifPresent(entity -> {
            if (isActiveProfile(entity)) {
                throw new AppBadException("Invalid email or phone number");
            } else if (isRegistrationProfile(entity)) {
                cleaner.accept(identifier);
            }
        });
        return true;
    }

    private boolean isRegistrationProfile(ProfileEntity entity) {
        return Objects.equals(entity.getStatus(), ProfileStatus.REGISTRATION);
    }

    private boolean isActiveProfile(ProfileEntity entity) {
        return Objects.equals(entity.getStatus(), ProfileStatus.ACTIVE) ||
                Objects.equals(entity.getStatus(), ProfileStatus.NOT_ACTIVE);
    }

    @Override
    @Cacheable(value = "customer",
            key = "T(com.example.customer.util.CacheKeyGenerator).generateCustomerKey(#dto.email(),#dto.phone())",
            unless = "#result==null||#result.isExpired()")
    public AuthTokenResponse loge(ProfileLoginRequestDTO dto, AppLanguage appLanguage) {

        var username = getUsername(dto.email(), dto.phone());
        if (!profileRepository.existsActiveByEmailOrPhone(dto.email(), dto.phone())) {
            throw new AppBadException(resourceBundleService.getMessage(ITEM_NOT_FOUND, appLanguage));
        }
        return keycloakService.getToken(username, dto);
    }


    @Override
    public Boolean smsVerification(String phone, String code, AppLanguage appLanguage) {

        BiConsumer<Boolean, String> throwIf = (condition, messageKey) -> {
            if (condition) {
                throw new AppBadException(resourceBundleService.getMessage(messageKey, appLanguage));
            }
        };
        throwIf.accept(notificationService.isBlocked(phone), TO_MANY_ATTEMPT_PLEASE);

        var profileEntity = profileRepository.findByPhone(phone).orElseThrow(() ->
                new AppBadException(resourceBundleService.getMessage(ITEM_NOT_FOUND, appLanguage)));

        throwIf.accept(isActiveProfile(profileEntity), THIS_PHONE_HAS_BEEN);

        var sms = smsServerService.findByPhoneNumber(profileEntity.getPhone(), appLanguage);

        throwIf.accept(Objects.equals(sms.getStatus(), SmsStatus.USED), THIS_PHONE_HAS_BEEN);
        throwIf.accept(!Objects.equals(sms.getCode(), code), PHONE_PASSWORD_WRONG);

        throwIf.accept(checkAttemptLimits(phone), TO_MANY_ATTEMPT_PLEASE);

        profileEntity.setStatus(ProfileStatus.ACTIVE);
        profileRepository.save(profileEntity);

        notificationService.recordLoginAttempt(phone);
        return true;
    }

    private boolean checkAttemptLimits(String phone) {
        var from = LocalDateTime.now().minusMinutes(1);
        var to = LocalDateTime.now();
        return smsServerService.getCountSendSms(phone, from, to) >= 3;
    }

    @Override
    public Boolean emailVerification(String token) {
        log.warn("verification token {}", token);
        var jwtDTO = decode(token);
        return profileRepository.getId(jwtDTO.getEmail())
                .map(entity -> {
                            if (Objects.equals(entity.getStatus(), ProfileStatus.ACTIVE)) {
                                log.warn("verification token already active email {}", jwtDTO.getEmail());
                                throw new AppBadException(resourceBundleService.getMessage(THIS_EMAIL_HAS_BEEN_REGISTERED, jwtDTO.getAppLanguage()));
                            }
                            log.warn("verification token already active email {}", jwtDTO.getEmail());
                            profileRepository.update(ProfileStatus.ACTIVE, jwtDTO.getEmail());
                            return true;
                        }
                ).orElseThrow(() -> new AppBadException(resourceBundleService.getMessage(ITEM_NOT_FOUND, jwtDTO.getAppLanguage())));

    }

    private boolean getProfileCheck(String identifier, AppLanguage language) {
        if (notificationService.isBlocked(identifier)) {
            throw new AppBadException(resourceBundleService.getMessage(TO_MANY_ATTEMPT_PLEASE, language));
        }
        int record = notificationService.recordLoginAttempt(identifier);
        log.warn("record count 😅 {}", record);
        return true;
    }


    private void doLongRunningTask() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}