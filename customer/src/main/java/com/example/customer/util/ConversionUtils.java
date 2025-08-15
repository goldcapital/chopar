package com.example.customer.util;

import com.example.customer.request.CustomerRequest;
import lombok.experimental.UtilityClass;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

@UtilityClass
public class ConversionUtils {

    public static UserRepresentation makeUserRepresentation(CustomerRequest registrationRecord) {
        var user = new UserRepresentation();
        user.setEnabled(true);
        if (registrationRecord.email() != null && !registrationRecord.email().isEmpty()) {
            user.setEmail(registrationRecord.email());
            user.setUsername(registrationRecord.email());
            user.setEmailVerified(true);
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
}
