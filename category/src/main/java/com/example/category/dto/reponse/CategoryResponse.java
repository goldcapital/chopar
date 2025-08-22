package com.example.category.dto.reponse;

import java.time.LocalDateTime;

public record CategoryResponse(
        Long id,
        String orderNumber,
        String nameUz,
        String nameRu,
        String nameEn,
        String regionCode,
        LocalDateTime createDate,
        LocalDateTime updateDate
) {
}
