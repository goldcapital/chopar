package com.example.customer.util;


import com.example.customer.config.AuthProperties;
import com.example.customer.dto.request.CustomerRequest;
import com.example.customer.dto.request.ProfileLoginRequestDTO;
import com.example.customer.dto.request.ProfileUpdateRequest;
import com.example.customer.dto.response.AuthTokenResponse;
import com.example.customer.exp.AppBadException;
import lombok.experimental.UtilityClass;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;
import java.util.Optional;

@UtilityClass
public class ConversionUtils {
    public static AuthTokenResponse convertResponseToAuthToken(AccessTokenResponse token) {
        return new AuthTokenResponse(token.getToken(), token.getTokenType(), token.getRefreshToken(), token.getExpiresIn(), token.getScope(), 0, token.getSessionState());
    }

    public static UserRepresentation makeUserRepresentation(CustomerRequest registrationRecord) {
        var user = new UserRepresentation();
        user.setEnabled(true);//user login qilib kira olishi uchun yaniy aktiv holatda
        if (registrationRecord.email() != null && !registrationRecord.email().isEmpty()) {
            user.setEmail(registrationRecord.email());
            user.setUsername(registrationRecord.email());
            user.setEmailVerified(true);//email allaqachon tasdiqlangan
        } else {
            user.setUsername(registrationRecord.phone());
        }

        user.setLastName(registrationRecord.lastname());
        user.setFirstName(registrationRecord.firstname());

        var credential = new CredentialRepresentation();
        credential.setValue(registrationRecord.password());
        credential.setTemporary(Boolean.FALSE);
        credential.setType(CredentialRepresentation.PASSWORD);

        user.setCredentials(List.of(credential));
        return user;
    }

    public static void updateUserFields(UserRepresentation user, ProfileUpdateRequest request) {

        if (request.email() != null)
            user.setEmail(request.email());
        user.setUsername(request.email());
        if (request.phone() != null && (request.email() == null || request.email().isEmpty())) {
            user.setUsername(request.phone());
        }
        if (request.password() != null) {
            var credential = new CredentialRepresentation();
            credential.setValue(request.password());
            credential.setTemporary(Boolean.FALSE);//parol vaqtinchalik emas
            credential.setType(CredentialRepresentation.PASSWORD);
            user.setCredentials(List.of(credential));
        }
    }

    public static Keycloak keycloakBuilder(String username, ProfileLoginRequestDTO request, AuthProperties properties) {
        //  var username = request.email() != null ? request.email() : request.phone() != null ?request.phone(): null;


        return KeycloakBuilder.builder()
                .username(username)
                .password(request.password())
                .serverUrl(properties.getServerUrl())
                .realm(properties.getRealm())
                .clientId(properties.getClientId())
                .clientSecret(properties.getClientSecret())
                .grantType(OAuth2Constants.PASSWORD)
                .build();

    }

    public static String getUsername(String email, String phone) {
        return Optional.ofNullable(email).or(() ->
                Optional.ofNullable(phone)).orElseThrow(() ->
                new AppBadException("You must enter an email or phone number."));

    }

}
