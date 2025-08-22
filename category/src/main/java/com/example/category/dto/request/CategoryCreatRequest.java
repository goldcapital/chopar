package com.example.category.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CategoryCreatRequest(
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
