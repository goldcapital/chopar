package com.example.customer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "kc-props")
public class AuthProperties {
    private String username;
    private String password;
    private String clientId;
    private String clientSecret;
    private String realm;
    private String serverUrl;
}
