package com.example.chopar_1.entity;

import com.example.chopar_1.enums.SmsStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sms_history")
public class SmsHistoryEntity extends BastEntity {
    @Column(name = "phone")
    private String phone;

    @Column(name = "message",columnDefinition = "TEXT")
    private  String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SmsStatus status;
}
