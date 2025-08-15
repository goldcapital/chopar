package com.example.customer.dto.response;

public record SmsResponse(
        String message,
        Integer fromData
) {
}
