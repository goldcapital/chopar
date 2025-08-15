package com.example.customer.entity;

import jakarta.persistence.Entity;
import lombok.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProductEntity extends BastEntity {

    private String nameUz;

    private String nameRu;

    private String descriptionUz;

    private String descriptionRu;

    private Double price;

    private Double discountPrice;

    private Long prtId;

    private Double rating;

    private Integer viewCount;

    private Long categoryId;
}
