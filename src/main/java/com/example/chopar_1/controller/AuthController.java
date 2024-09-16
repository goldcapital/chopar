package com.example.chopar_1.controller;

import com.example.chopar_1.dto.ProfileDTO;
import com.example.chopar_1.dto.request.ProfileLoginRequestDTO;
import com.example.chopar_1.entity.SmsHistoryEntity;
import com.example.chopar_1.enums.AppLanguage;
import com.example.chopar_1.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@Tag(name = "Authorization Api list",description = "Api list for Authorization")
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    @Autowired
    private  AuthService authService;
    @GetMapping("/loge")
    @Operation(summary = "Api for login", description = "this api used for authorization")
    public ResponseEntity<ProfileDTO>loge(@RequestBody ProfileLoginRequestDTO dto,
                                          @RequestHeader(value = "Accept-Language",defaultValue = "uz")AppLanguage appLanguage){
       return ResponseEntity.ok( authService.loge(dto,appLanguage));
    }

@Operation(summary = "Api for email  registration",description = "this api used for registration email")
    @PostMapping("/registration/email")
    public ResponseEntity<ProfileDTO> registrationEmail(@RequestBody ProfileDTO dto,
                                                   @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLanguage appLanguage) {
        log.info("registration Email", dto.getEmail());
        return ResponseEntity.ok(authService.registrationEmail(dto, appLanguage));
    }
    @Operation(summary = "Api for phone registration",description = "this api used for registration phone")
    @PostMapping("/registration/phone")
    public ResponseEntity<SmsHistoryEntity> registrationPhone(@RequestBody ProfileDTO dto,
                                                              @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLanguage appLanguage) {
        log.info("registration Phone",dto.getPhone());

        return ResponseEntity.ok(authService.registrationPhone(dto, appLanguage));
    }
    @Operation(summary = "Api for email code Verification",description = "this api used for verification email")
   @GetMapping("/verification/email/{id}")
    public ResponseEntity<Boolean> emailVerification(@PathVariable("id") String id) {
        return ResponseEntity.ok(authService.emailVerification(id));
    }

    @Operation(summary = "Api for Phone code  Verification",description = "this api used for verification phone")
    @PostMapping("/verification/phone")
    public ResponseEntity<Boolean> smsVerification(@RequestBody  String code,
                                                   @RequestHeader(value ="Authorization") String jwt ) {
        String token = jwt.replace("Bearer ", "");
        //log.info("registration", dto.getEmail(), dto.getPhone());
        return ResponseEntity.ok(authService.smsVerification(code, token));
    }
}
