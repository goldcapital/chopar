package com.example.customer.service;

import com.example.customer.entity.SmsHistoryEntity;
import com.example.customer.enums.AppLanguage;

import java.time.LocalDateTime;

public interface SmsServerService {

    SmsHistoryEntity findByPhoneNumber(String phoneNumber, AppLanguage language);

    Long getCountSendSms(String phone, LocalDateTime from, LocalDateTime to);

}
