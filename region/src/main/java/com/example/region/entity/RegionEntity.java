package com.example.region.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "region")
public class RegionEntity {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;
    private Long orderNumber;
    private String nameUZ;
    private String nameRu;
    private String nameEn;
    private String regionCode;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createDate;
    @UpdateTimestamp
    private LocalDateTime updateDate;
    @Column(name = "visible")
    private Boolean visible = true;
}
