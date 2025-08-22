package com.example.customer.entity;


import com.example.customer.enums.ProfileRole;
import com.example.customer.enums.ProfileStatus;
import jakarta.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;


import java.time.LocalDate;


@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "profile")
public class ProfileEntity extends BastEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;
    private String username;

    @Column(name = "email")
    private String email;//(un,no t null),

    @Column(name = "password")
    private String password;
    private String keycloakId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private ProfileStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private ProfileRole role;

    @Column(name = "birth_data")
    private LocalDate birthData;

}
