package com.example.chopar_1.entity;

import jakarta.persistence.Column;

public class Order_busketEntity extends BastEntity {
    @Column(name = "product_id",nullable = false)
   private String product_id;
   @Column(name = "order_id",nullable = false)

     private String order_id;
    @Column(name = "amount", nullable = false)
    private Double amount;
    @Column(name = "price")
    private Double price;
   // id,product_id,order_id, amount, price, created_date
}
