package com.example.customer.service;


import com.example.customer.dto.ProfileDTO;

import com.example.customer.dto.request.CustomerRequest;
import com.example.customer.dto.request.ProfileLoginRequestDTO;

import com.example.customer.dto.response.AuthTokenResponse;
import com.example.customer.enums.AppLanguage;

public interface AuthService {
    Boolean smsVerification(String phone, String code, AppLanguage appLanguage);

    AuthTokenResponse loge(ProfileLoginRequestDTO dto, AppLanguage appLanguage);

    Boolean registration(CustomerRequest dto, AppLanguage appLanguage);

    Boolean emailVerification(String token);
}