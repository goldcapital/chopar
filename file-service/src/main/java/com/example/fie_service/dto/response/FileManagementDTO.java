package com.example.fie_service.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record FileManagementDTO(
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        UUID id,
        String fileName,

        String filePath,

        String extension,

        String bucketName
) {
}
