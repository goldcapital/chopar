package com.example.region.repository;

import com.example.region.entity.RegionEntity;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RegionRep extends JpaRepository<RegionEntity, Long> {
    Boolean existsByNameUZAndVisible(String nameUz, Boolean visible);

    Boolean existsByNameEnAndVisible(String nameEn, Boolean visible);

    Boolean existsByNameRuAndVisible(String nameRU, Boolean visible);

    Optional<RegionEntity> findByIdAndVisible(Long id, Boolean visible);
    List<RegionEntity> findAllByVisible(PageRequest pageable, Boolean visible);
}
