package com.example.chopar_1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "order_entity")
public class OrderEntity extends BastEntity {

  /*  @Column(name = "profile_id", nullable = false)
    private Long profileId;*/

    @Column(name = "amount", nullable = false)
    private Double amount;

 /*   @Column(name = "created_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate=L;*/

    @Column(name = "delivered_address", nullable = false)
    private String deliveredAddress;

    @Column(name = "delivered_contact", nullable = false)
    private String deliveredContact;
    /*  @Column(name = "delivered_contact")
     private  String delivered_contact;*/

    @Column(name = "profile_id")
    private String profileId;
    @ManyToOne
    @JoinColumn(name = "profile_id",updatable = false,insertable = false)
    private ProfileEntity profile;
}
