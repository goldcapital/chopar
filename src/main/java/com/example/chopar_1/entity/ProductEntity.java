package com.example.chopar_1.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product")
public class ProductEntity extends BastEntity{

    @Column(name = "name_uz")
    private String nameUz;

    @Column(name = "name_ru")
    private String nameRu;

    @Column(name = "description_uz")
    private String descriptionUz;

    @Column(name = "description_ru")
    private String descriptionRu;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "discount_price")
    private Double discountPrice;

    @Column(name = "prt_id")
    private Long prtId;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "view_count")
    private Integer viewCount;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;
}
