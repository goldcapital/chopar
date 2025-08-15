package com.example.customer.service;


import com.example.customer.dto.ProfileDTO;
import com.example.customer.dto.request.CustomerRequestPhone;
import com.example.customer.dto.request.ProfileLoginRequestDTO;
import com.example.customer.dto.response.SmsResponse;
import com.example.customer.enums.AppLanguage;

public interface AuthService {
     SmsResponse registrationPhone(CustomerRequestPhone dto, AppLanguage appLanguage);
     Boolean smsVerification(String phone, String code, AppLanguage appLanguage);
     ProfileDTO loge(ProfileLoginRequestDTO dto, AppLanguage appLanguage) ;
     ProfileDTO registrationEmail(ProfileDTO dto, AppLanguage appLanguage);
     Boolean emailVerification(String token);
}