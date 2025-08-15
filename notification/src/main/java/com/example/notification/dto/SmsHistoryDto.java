package com.example.notification.dto;

import com.example.notification.enums.SmsStatus;

public record SmsHistoryDto(
        String phoneNumber,
        SmsStatus status,

        String code
) {
}
