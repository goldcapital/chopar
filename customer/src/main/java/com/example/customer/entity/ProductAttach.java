package com.example.customer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "product_attach_entity")
public class ProductAttach extends BastEntity {
    @Column(name = "product_id", nullable = false)
    private Long productId;
    @Column(name = "attach_id", nullable = false)
    private Long  attachId;

}
