package com.example.customer.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public record AuthTokenResponse(

        @JsonProperty(value = "access_token")
        String accessToken,


        @JsonProperty(value = "token_type")
        String tokenType,


        @JsonProperty(value = "refresh_token")
        String refreshToken,


        @JsonProperty(value = "expires_in")
        Long expiresIn,


        String scope,


        int iat,


        String jti

) implements Serializable {
    public boolean isExpired() {
        return expiresIn < 360;
    }

}
