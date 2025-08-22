package com.example.region.dto.response;

import java.time.LocalDateTime;

public record RegionResponse(
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
