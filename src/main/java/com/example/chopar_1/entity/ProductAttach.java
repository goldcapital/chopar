package com.example.chopar_1.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product_attach")
public class ProductAttach extends BastEntity {
    @Column(name = "product_id", nullable = false)
    private Long productId;
    @Column(name = "attach_id", nullable = false)
    private Long  attachId;

}
