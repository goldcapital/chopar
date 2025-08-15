package com.example.notification.service;


import com.example.notification.enums.AppLanguage;

import java.time.LocalDateTime;


public interface SmsHistoryService {

     void crate(String phoneNumber);
     boolean updateByPhone(String phone) ;
     Boolean getByPhoneCheck(String phone, String code, AppLanguage appLanguage);
     Long getCountSendSms(String phone, LocalDateTime from, LocalDateTime to) ;

    }
