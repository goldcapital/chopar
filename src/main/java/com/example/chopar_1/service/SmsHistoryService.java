package com.example.chopar_1.service;

import com.example.chopar_1.entity.SmsHistoryEntity;
import com.example.chopar_1.enums.AppLanguage;
import com.example.chopar_1.enums.SmsStatus;
import com.example.chopar_1.exp.AppBadException;
import com.example.chopar_1.repository.SmsRepository;
import com.example.chopar_1.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SmsHistoryService {
    private final SmsServerService smsServerService;
    private  final SmsRepository smsRepository;
    private final ResourceBundleService resourceBundleService;

    public SmsHistoryEntity crate(String phoneNumber){

   String code=RandomUtil.getRandomSmsCode();
    var entity = new SmsHistoryEntity();
    entity.setPhone(phoneNumber);
    entity.setStatus(SmsStatus.NEW);
    entity.setCode(code);


    smsRepository.save(entity);
    smsServerService.send(entity.getPhone(), "www.Tutorchat.uz sayti uchun akkauntni tasdiqlash kodi:", code);

        var sms = new SmsHistoryEntity();
        sms.setMessage("iltimos codni kiriting");
        sms.setFromDate(40);
        return sms;
}

    public boolean updateByPhone(String phone) {
        String code= RandomUtil.getRandomSmsCode();

        var entity = new SmsHistoryEntity();
        entity.setPhone(phone);
        entity.setStatus(SmsStatus.NEW);
        entity.setCode(code);
        smsRepository.save(entity);
        smsServerService.send(entity.getPhone(), "www.Tutorchat.uz sayti uchun akkauntni tasdiqlash kodi:", code);

        return false;
    }

    public Boolean getByPhoneCheck(String phone, String code, AppLanguage appLanguage) {

        var from = LocalDateTime.now().minus(Duration.ofSeconds(40));
        var to = LocalDateTime.now();

        SmsHistoryEntity smsHistoryEntity=smsRepository.gedCodeByPhone(phone,from,to);

        if(smsHistoryEntity==null||!smsHistoryEntity.getCode().trim().equals(code.trim())
                ||!smsHistoryEntity.getStatus().equals(SmsStatus.NEW)){
            throw new AppBadException(resourceBundleService.getMessage("phone.password.wrong",appLanguage));
        }

        smsRepository.updateByStatusAndPhone(SmsStatus.USED,phone);

        return true;
    }

    public Long getCountSendSms(String phone, LocalDateTime from, LocalDateTime to) {
        return smsRepository.countSendSms(phone, from, to);

    }
}
