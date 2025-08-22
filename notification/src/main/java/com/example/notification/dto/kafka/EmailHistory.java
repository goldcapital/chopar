package com.example.notification.dto.kafka;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record EmailHistory(
        String email,
        @Column(columnDefinition = "TEXT") String message,
        String name,
        @Column(columnDefinition = "TEXT")  String jwt
) {
}
