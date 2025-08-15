package com.example.notification.controller;

import com.example.notification.enums.AppLanguage;
import com.example.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/get-count-send-email")
    public ResponseEntity<Long> getCountSendEmail(
            @RequestParam String email,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        return ResponseEntity.ok(notificationService.getCountSendEmail(email, from, to));
    }

    @GetMapping("/get-count-sms")
    public ResponseEntity<Long> getCountSendPhone(
            @RequestParam String phone,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        return ResponseEntity.ok(notificationService.getCountSendPhone(phone, from, to));
    }

    @GetMapping("/phone-check")
    public ResponseEntity<Boolean> checkPhoneVerification(
            @RequestParam String phone,
            @RequestParam String code,
            @RequestParam(required = false, defaultValue = "UZ") AppLanguage appLanguage) {
        return ResponseEntity.ok(notificationService.getByPhoneCheck(phone, code, appLanguage));
    }
}
