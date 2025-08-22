package com.example.fie_service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioProps {
    private String url;
    private String accessKey;
    private String secretKey;
}
