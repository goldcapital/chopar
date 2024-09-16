package com.example.chopar_1.entity;

import com.example.chopar_1.enums.SmsStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sms_history")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SmsHistoryEntity extends BastEntity {
    @Column(name = "code")
    private  String code;

    @Column(name = "phone")
    private String phone;

    @Column(name = "message",columnDefinition = "TEXT")
    private  String message;

    @Column (name = "jwt")
    private String jwt;
    @Column(name = "from_date")
    private Integer fromDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SmsStatus status;
}
