package com.example.notification.entity;



import com.example.notification.enums.SmsStatus;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SmsStatus status;
}
