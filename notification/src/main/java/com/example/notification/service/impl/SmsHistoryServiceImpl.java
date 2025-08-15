package com.example.notification.service.impl;

import com.example.notification.dto.SmsHistoryDto;
import com.example.notification.entity.SmsHistoryEntity;
import com.example.notification.enums.AppLanguage;
import com.example.notification.enums.SmsStatus;
import com.example.notification.exp.AppBadException;
import com.example.notification.mapper.SmsHistoryMapper;
import com.example.notification.repository.SmsRepository;
import com.example.notification.service.ResourceBundleService;
import com.example.notification.service.SmsHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

import static com.example.notification.util.RandomUtil.getRandomSmsCode;

@Service
@RequiredArgsConstructor
public class SmsHistoryServiceImpl implements SmsHistoryService {
    // private final SmsServerService smsServerService;
    private final SmsRepository smsRepository;
    private final ResourceBundleService resourceBundleService;
    private final SmsHistoryMapper smsHistoryMapper;

    public void crate(String phoneNumber) {
        var code = getRandomSmsCode();
        var entity = smsHistoryMapper.toEntity(new SmsHistoryDto(phoneNumber, SmsStatus.NEW, code));
        smsRepository.save(entity);
       // smsServerService.send(entity.getPhone(), "www.Tutorchat.uz sayti uchun akkauntni tasdiqlash kodi:", code);

    }

    public boolean updateByPhone(String phone) {
        String code = getRandomSmsCode();

        var entity = new SmsHistoryEntity();
        entity.setPhone(phone);
        entity.setStatus(SmsStatus.NEW);
        entity.setCode(code);
        smsRepository.save(entity);
       // smsServerService.send(entity.getPhone(), "www.Tutorchat.uz sayti uchun akkauntni tasdiqlash kodi:", code);

        return false;
    }

    @Override
    public Boolean getByPhoneCheck(String phone, String code, AppLanguage appLanguage) {

        var from = LocalDateTime.now().minus(Duration.ofSeconds(40));
        var to = LocalDateTime.now();
        SmsHistoryEntity smsHistoryEntity = smsRepository.gedCodeByPhone(phone, from, to);

        if (smsHistoryEntity == null || !smsHistoryEntity.getCode().trim().equals(code.trim())
                || !smsHistoryEntity.getStatus().equals(SmsStatus.NEW)) {
            throw new AppBadException(resourceBundleService.getMessage("phone.password.wrong", appLanguage));
        }
        smsRepository.updateByStatusAndPhone(SmsStatus.USED, phone);

        return true;
    }

    public Long getCountSendSms(String phone, LocalDateTime from, LocalDateTime to) {
        return smsRepository.countSendSms(phone, from, to);

    }
}
