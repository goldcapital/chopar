package com.example.customer.service;

import com.example.customer.config.AuthProperties;
import com.example.customer.exp.AppBadException;
import com.example.customer.repository.ProfileRepository;
import com.example.customer.request.CustomerRequest;
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
import static com.example.customer.util.ConversionUtils.makeUserRepresentation;
import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakService {
    private final Keycloak keycloak;
    private final AuthProperties authProperties;

    public String creatKeycloakUser(CustomerRequest customerRequest) {
         var  username = "";
        if (!Objects.isNull(customerRequest.email())) {
            username = customerRequest.email();
        } else if (!Objects.isNull(customerRequest.phone())) {
            username = customerRequest.phone();
        }

        try {
            var response = mainResource().create(makeUserRepresentation(customerRequest));
            if (response.getStatus() == 409) {
                throw new BadRequestException(format(KEYCLOAK_RESPONSE_STATUS_409, username));
            }
            if (response.getStatus() != 201 && response.getStatus() != 204) {
                throw new RuntimeException(format(KEYCLOAK_RESPONSE_STATUS_201_AND_204, response.getStatus(),username));
            }
            return getKeycloakUser(username).getId();

        } catch (ProcessingException e) {
            throw new RuntimeException(e);
        }


    }

    private UserRepresentation getKeycloakUser(String username) {
        var response = mainResource().search(username,true);

        if (Objects.isNull(response)||response.isEmpty()) {
            log.warn(USERNAME_NOT_FOUND, username);
            throw new AppBadException(format(USERNAME_NOT_FOUND, username));
        }

        if (response.size() > 1) {
            log.warn("More than one Keycloak user found with username/username: {}", username);
        }
        return response.get(0);
    }

    private UsersResource mainResource() {
        return keycloak.realm(authProperties.getRealm()).users();
    }
}