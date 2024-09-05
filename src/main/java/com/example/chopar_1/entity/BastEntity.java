package com.example.chopar_1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public class BastEntity {
    @Id
  /*  @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;*/
   @GeneratedValue(generator = "uuid-hibernate-generator")
    @GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
   private String id;

    @Column(name = "visible")
    private Boolean visible=true;

    @Column(name = "created_date")
    private LocalDateTime createdDate=LocalDateTime.now();

}
