package com.example.chopar_1.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "email_history")
public class EmailHistoryEntity extends BastEntity {

    @Column(name = "message",columnDefinition = "TEXT")
   private String message;
    @Column(name = "email")
   private  String email;
}
