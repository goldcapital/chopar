package com.example.customer.config;

import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(AuthProperties.class)
public class KeycloakConfig {
    private final AuthProperties authProperties;
    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(authProperties.getServerUrl())
                .realm(authProperties.getRealm())
                .clientSecret(authProperties.getClientSecret())
                .clientId(authProperties.getClientId())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .build();
    }
}
