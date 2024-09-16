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
import com.example.chopar_1.repository.SmsRepository;
import com.example.chopar_1.util.JWTUtil;
import com.example.chopar_1.util.MDUtil;
import com.example.chopar_1.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.chopar_1.util.JWTUtil.decode;

@Service
public class AuthService {
   @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ResourceBundleService resourceBundleService;
    @Autowired
    private MailSenderService mailSenderService;
    @Autowired
     private EmailHistoryRepository emailHistoryRepository;
    @Autowired
    private SmsRepository smsRepository;
    @Autowired
    private SmsServerService smsServerService;

    public SmsHistoryEntity registrationPhone(ProfileDTO dto, AppLanguage appLanguage) {

        get(dto,appLanguage);
        //var from=LocalDateTime.now().minus(Duration.ofSeconds(40));
        //   var to=LocalDateTime.now();

        String code = RandomUtil.getRandomSmsCode();

        var profileEntity=new ProfileEntity();

        profileEntity.setName(dto.getName());
        profileEntity.setPassword(MDUtil.encode(dto.getPassword()));
        profileEntity.setRole(ProfileRole.ROLE_USER);
        profileEntity.setPhone(dto.getPhone());
        profileEntity.setStatus(ProfileStatus.REGISTRATION);
        String jwt=JWTUtil.encode(dto.getPhone(),profileEntity.getEmail(),ProfileRole.ROLE_USER,appLanguage);
        profileRepository.save(profileEntity);


        var entity=new SmsHistoryEntity();
        entity.setPhone(dto.getPhone());
        entity.setCode(code);
        entity.setPhone(dto.getPhone());
        smsRepository .save(entity);
        smsServerService.send(entity.getPhone(), "www.Tutorchat.uz sayti uchun akkauntni tasdiqlash kodi:", code);

        var sms=new SmsHistoryEntity();
        sms.setJwt(jwt);
        sms.setFromDate(40);
        return sms;
    }
    public ProfileDTO registrationEmail(ProfileDTO dto, AppLanguage appLanguage) {
        get(dto,appLanguage);


        ProfileEntity entity = new ProfileEntity();

        entity.setRole(ProfileRole.ROLE_USER);
        entity.setEmail(dto.getEmail());
        entity.setPassword(MDUtil.encode(dto.getPassword()));
        entity.setStatus(ProfileStatus.REGISTRATION);
        entity.setName(dto.getName());

        entity.setJwt(JWTUtil.encode(entity.getPhone(),entity.getEmail(), entity.getRole(), appLanguage));
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
        EmailHistoryEntity emailHistoryEntity=new EmailHistoryEntity();
        emailHistoryEntity.setEmail(dto.getEmail());
        emailHistoryEntity.setMessage(text);
        emailHistoryRepository.save(emailHistoryEntity);
        return dto;
    }
    public Boolean smsVerification(String code,String token) {

        JwtDTO jwtDTO = decode(token);

        var from = LocalDateTime.now().minus(Duration.ofSeconds(40));
        var to = LocalDateTime.now();
        Optional<ProfileEntity> optional = profileRepository.findByPhone(jwtDTO.getPhone());
        if (optional.isPresent()) {
            if (!optional.get().getStatus().equals(ProfileStatus.ACTIVE)) {
                var codes = smsRepository.gedCodeByPhone(jwtDTO.getPhone(), from, to);
                if (codes == null) {
                    codes.trim();
                    if (codes.equals(code.trim())) {
                        profileRepository.updateByPhone(jwtDTO.getPhone(), ProfileStatus.ACTIVE);
                    }
                    throw new AppBadException(resourceBundleService.getMessage("phone.password.wrong", jwtDTO.getAppLanguage()));
                }
                throw new AppBadException(resourceBundleService.getMessage("item.not.found", jwtDTO.getAppLanguage()));//
            }
            throw new AppBadException(resourceBundleService.getMessage("This.phone.has.been.registered", jwtDTO.getAppLanguage()));
        }
       throw new AppBadException(resourceBundleService.getMessage("please.tyre.again",jwtDTO.getAppLanguage()));
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
        throw new AppBadException(resourceBundleService.getMessage("This.email.has.been.registered",jwtDTO.getAppLanguage()));

    }

    private ProfileEntity get(ProfileDTO dto,AppLanguage language){
             Optional<ProfileEntity>optional=profileRepository.findByEmailOrPhone(dto.getEmail(),dto.getPhone());
             if (optional.isPresent() ){

             if (optional.get().getStatus().equals(ProfileStatus.REGISTRATION)) {
                profileRepository.deleteByEmail(optional.get().getEmail());

                LocalDateTime from = LocalDateTime.now().minusMinutes(1);
                LocalDateTime to = LocalDateTime.now();
                 ProfileEntity entity= optional.get();

                   if(dto.getEmail()!=null) {
                       if (emailHistoryRepository.countSendEmail(dto.getEmail(),  from, to) >= 3) {
                           throw new AppBadException(resourceBundleService.getMessage("To.many.attempt.Please.try.after.one.minute", language));
                       }
                      return entity;
                   }
                  if (smsRepository.countSendSms(dto.getPhone(),from,to)>=3){
                      throw new AppBadException(resourceBundleService.getMessage("To.many.attempt.Please.try.after.one.minute", language));
                  }
                return entity;
            }
            throw new AppBadException(resourceBundleService.getMessage("This.email.has.been.registered", language));
        }
        return null;
    }
    public ProfileDTO loge(ProfileLoginRequestDTO dto, AppLanguage appLanguage) {
        Optional<ProfileEntity> optional = profileRepository.findByEmailOrPhone(dto.getEmail(), dto.getPhone());

        if (optional.isPresent()) {
            if (optional.get().getStatus().equals(ProfileStatus.ACTIVE)
                    && optional.get().getPassword().
                    equals(MDUtil.encode(dto.getPassword()))) {
                 // throw new AppBadException(resourceBundleService.getMessage("item.not.found",appLanguage));
                ProfileEntity entity = optional.get();
                ProfileDTO profileDTO = new ProfileDTO();
                profileDTO.setName(entity.getName());
                profileDTO.setJwt(JWTUtil.encode(dto.getPhone(),dto.getEmail(), entity.getRole(), appLanguage));
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