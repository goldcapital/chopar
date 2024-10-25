package com.example.chopar_1.entity;

import com.example.chopar_1.enums.ProfileRole;
import com.example.chopar_1.enums.ProfileStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "profile")
public class ProfileEntity extends BastEntity  {


    @Column(name = "name")
    private String  name;

    @Column(name = "phone")
    private  String phone;

    @Column(name = "email")
    private String  email;//(un,no t null),

    @Column(name = "password")
    private  String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProfileStatus  status;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private ProfileRole role;

    @Column(name = "birth_data")
  private LocalDateTime birthDate;

    @Column(name = "jwt",columnDefinition = "Text")
    private String jwt;

}
