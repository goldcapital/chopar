package com.example.customer.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class OrderBusketEntity extends BastEntity {

    @Column(name = "product_id", nullable = false)
    private String productId;
    @ManyToOne
    @JoinColumn(name = "product", updatable = false, insertable = false)
    private ProductEntity productEntity;

    @Column(name = "order_id", nullable = false)
    private String orderId;
    @ManyToOne
    @JoinColumn(name = "order_id", updatable = false, insertable = false)
    private OrderEntity order;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "price")
    private Double price;
}
