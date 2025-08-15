package com.example.notification.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "email_history")
@EqualsAndHashCode(callSuper = true)
public class EmailHistoryEntity extends BastEntity {

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;
    @Column(name = "email")
    private String email;
}
