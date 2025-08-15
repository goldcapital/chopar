package com.example.notification.service;

import com.example.notification.enums.AppLanguage;

import java.time.LocalDateTime;

public interface NotificationService {
    Long getCountSendEmail(String email, LocalDateTime from, LocalDateTime to);

    Long getCountSendPhone(String phone, LocalDateTime from, LocalDateTime to);

    Boolean getByPhoneCheck(String phone, String code, AppLanguage appLanguage);
}
