package com.example.customer.service;

import com.example.customer.dto.request.CustomerRequest;
import com.example.customer.dto.request.ProfileLoginRequestDTO;
import com.example.customer.dto.request.ProfileUpdateRequest;
import com.example.customer.dto.response.AuthTokenResponse;
import com.example.customer.enums.AppLanguage;

public interface KeycloakService {


     String creatKeycloakUser(CustomerRequest customerRequest);


     AuthTokenResponse getToken(String username, ProfileLoginRequestDTO dto);
     Boolean updateKeycloakUser(String username, ProfileUpdateRequest request, AppLanguage language);

    void deleteByUsername(String username,AppLanguage language);
}