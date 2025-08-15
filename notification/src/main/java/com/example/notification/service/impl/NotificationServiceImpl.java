package com.example.notification.service.impl;

import com.example.notification.enums.AppLanguage;
import com.example.notification.repository.EmailHistoryRepository;
import com.example.notification.repository.SmsRepository;
import com.example.notification.service.NotificationService;
import com.example.notification.service.SmsHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final SmsRepository smsRepository;
    private final EmailHistoryRepository emailHistoryRepository;
    final SmsHistoryService smsHistoryService;

    @Override
    public Long getCountSendEmail(String email, LocalDateTime from, LocalDateTime to) {
        return emailHistoryRepository.countSendEmail(email, from, to);
    }

    @Override
    public Long getCountSendPhone(String phone, LocalDateTime from, LocalDateTime to) {
        return smsHistoryService.getCountSendSms(phone, from, to);
    }

    @Override
    public Boolean getByPhoneCheck(String phone, String code, AppLanguage appLanguage) {
        return smsHistoryService.getByPhoneCheck(phone, code, appLanguage);
    }
}
