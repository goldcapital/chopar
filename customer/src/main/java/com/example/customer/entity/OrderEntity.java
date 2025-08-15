package com.example.customer.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "order_entity")
public class OrderEntity extends BastEntity {


    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "delivered_address", nullable = false)
    private String deliveredAddress;

    @Column(name = "delivered_contact", nullable = false)
    private String deliveredContact;

    @Column(name = "profile_id")
    private String profileId;
    @ManyToOne
    @JoinColumn(name = "profile_id",updatable = false,insertable = false)
    private ProfileEntity profile;
}
