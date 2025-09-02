package com.example.customer.service.impl;

import com.example.customer.config.AuthProperties;
import com.example.customer.dto.request.CustomerRequest;
import com.example.customer.dto.request.ProfileLoginRequestDTO;
import com.example.customer.dto.request.ProfileUpdateRequest;
import com.example.customer.dto.response.AuthTokenResponse;
import com.example.customer.enums.AppLanguage;
import com.example.customer.exp.AppBadException;
import com.example.customer.service.KeycloakService;
import com.example.customer.service.ResourceBundleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ProcessingException;
import java.util.Objects;

import static com.example.customer.config.ErrorMessage.*;
import static com.example.customer.config.ErrorMessage.USERNAME_NOT_FOUND;
import static com.example.customer.config.ThrowIfMessage.ITEM_NOT_FOUND;
import static com.example.customer.util.ConversionUtils.*;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakServiceImpl implements KeycloakService {
    private final ResourceBundleService resourceBundleService;
    private final Keycloak keycloak;
    private final AuthProperties authProperties;
    private final Integer INDEX = 0;

    @Override
    public String creatKeycloakUser(CustomerRequest customerRequest) {
        var username = "";
        if (!Objects.isNull(customerRequest.email())) {
            username = customerRequest.email();
        } else if (!Objects.isNull(customerRequest.phone())) {
            username = customerRequest.phone();
        }

        try {
            var user = mainResource().search(username, true);

            if (user != null && !user.isEmpty()) {
                var userRepresentation = user.get(INDEX);
                mainResource().delete(userRepresentation.getId());
            }
            var response = mainResource().create(makeUserRepresentation(customerRequest));
            if (response.getStatus() == 409) {
                throw new BadRequestException(format(KEYCLOAK_RESPONSE_STATUS_409, username));
            }
            if (response.getStatus() != 201 && response.getStatus() != 204) {
                throw new RuntimeException(format(KEYCLOAK_RESPONSE_STATUS_201_AND_204, response.getStatus(), username));
            }
            return getKeycloakUser(username).getId();

        } catch (ProcessingException e) {
            throw new RuntimeException(e);
        }


    }

    private UserRepresentation getKeycloakUser(String username) {
        var response = mainResource().search(username, true);

        if (Objects.isNull(response) || response.isEmpty()) {
            log.warn(USERNAME_NOT_FOUND, username);
            throw new AppBadException(format(USERNAME_NOT_FOUND, username));
        }

        if (response.size() > 1) {
            log.warn("More than one Keycloak user found with username/username: {}", username);
        }
        return response.get(INDEX);
    }

    @Override
    public AuthTokenResponse getToken(String username, ProfileLoginRequestDTO dto) {


        try (var keycloakForToken = keycloakBuilder(username, dto, authProperties)) {
            var token = keycloakForToken.tokenManager().getAccessToken();
            log.info("RESPONSE from generateToken() -> SUCCESS");
            log.info("token life time -> {}", token.getExpiresIn());
            return convertResponseToAuthToken(token);
        } catch (Exception ex) {
            log.warn("Error occurred while generatingToken() -> {}", ex.getMessage());
            throw new BadRequestException(ex.getMessage());
        }
    }

    @Override
    public Boolean updateKeycloakUser(String username, ProfileUpdateRequest request, AppLanguage language) {
        var userRepresentations = getKeycloakUser(username);
        log.info("response_from_keycloak -> {}", userRepresentations);

        if (Objects.isNull(userRepresentations)) {
            log.warn("RESPONSE from updateUser -> {}", USER_NOT_FOUND);
            throw new BadRequestException(resourceBundleService.getMessage(ITEM_NOT_FOUND, language));
        }
        updateUserFields(userRepresentations, request);
        try {
            mainResource().get(userRepresentations.getId()).update(userRepresentations);
            log.info("RESPONSE from updateUser -> {}", userRepresentations);

        } catch (RuntimeException ex) {
            log.warn("Error happened while updateUser -> {}", ex.getMessage());
            throw new BadRequestException(ex.getMessage());

        }
        return true;
    }

    @Override
    public void deleteByUsername(String username,AppLanguage language) {
        var user = getKeycloakUser(username);
        if (Objects.isNull(user)) {
            log.warn("RESPONSE from updateUser -> {}", USER_NOT_FOUND);
            throw new BadRequestException(resourceBundleService.getMessage(ITEM_NOT_FOUND, language));
        }
        try {
            mainResource().delete(user.getId());
        }catch (RuntimeException ex) {
            log.warn("Error happened while updateUser -> {}", ex.getMessage());
            throw new BadRequestException(ex.getMessage());

        }

    }


    private UsersResource mainResource() {
        return keycloak.realm(authProperties.getRealm()).users();
    }
}
