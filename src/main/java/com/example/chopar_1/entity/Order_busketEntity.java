package com.example.chopar_1.entity;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;

public class Order_busketEntity extends BastEntity {

    @Column(name = "product_id",nullable = false)
    private String product_id;

    @JoinColumn(name = "product",updatable = false,insertable = false)
    private  ProductEntity productEntity;

    @Column(name = "order_id",nullable = false)
    private String order_id;

    @JoinColumn(name = "order_id",updatable = false,insertable = false)
   private  Order_busketEntity order_busketEntity;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "price")
    private Double price;
   // id,product_id,order_id, amount, price, created_date
}
