package com.example.customer.dto.request;

public record KafkaSendMessage(
        String email,
        String jwt,
        String name

) {
}
