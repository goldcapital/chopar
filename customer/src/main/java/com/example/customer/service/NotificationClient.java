package com.example.customer.service;

import com.example.customer.enums.AppLanguage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@FeignClient(name = "notification-service",
        url = "${app.config.notification-url}")
public interface NotificationClient {
    @GetMapping("/get-count-send-email")
    Integer getCountSendEmail(String email, LocalDateTime from, LocalDateTime to);

    @GetMapping("/get-count-sms")
    Long getCountSendSms(String phone, LocalDateTime from, LocalDateTime to);
    @GetMapping("/phone-check")
    Boolean getByPhoneCheck(String phone, String code, AppLanguage appLanguage);
}
