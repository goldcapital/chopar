package com.example.region.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RegionCreatRequest(
        Long orderNumber,
        @NotBlank
        String nameUz,
        @NotBlank
        String nameRu,
        @NotBlank
        String nameEn,
        String regionCode
) {
}
