package com.example.category.repository;

import aj.org.objectweb.asm.commons.Remapper;
import com.example.category.entity.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRep extends JpaRepository<CategoryEntity, Long> {
    Boolean existsByNameUzAndVisible(String nameUz, Boolean visible);

    Boolean existsByNameRuAndVisible(String nameRu, Boolean visible);

    Boolean existsByNameEnAndVisible(String nameEn, Boolean visible);

    Optional<CategoryEntity> findByIdAndVisible(Long id, Boolean aTrue);

    Page<CategoryEntity> findAllAndVisible(PageRequest pageable, Boolean aTrue);


}
