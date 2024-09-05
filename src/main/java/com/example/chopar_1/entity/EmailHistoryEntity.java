package com.example.chopar_1.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "email_history")
public class EmailHistoryEntity extends BastEntity {

    @Column(name = "message",columnDefinition = "TEXT")
   private String message;
    @Column(name = "email")
   private  String email;
}
