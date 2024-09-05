package com.example.chopar_1.controller;

import com.example.chopar_1.dto.ProfileDTO;
import com.example.chopar_1.enums.AppLanguage;
import com.example.chopar_1.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Authorization Api list",description = "Api list for Authorization")
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    @Autowired
    private  AuthService authService;
    @GetMapping("/loge")
    @Operation(summary = "Api for login", description = "this api used for authorization")
    public ResponseEntity<ProfileDTO>loge(@RequestBody ProfileDTO dto,
                                          @RequestHeader(value = "Accept-Language",defaultValue = "uz")AppLanguage appLanguage){
       return ResponseEntity.ok( authService.loge(dto,appLanguage));
    }

@Operation(summary = "Api for registration",description = "this api used for registration")
    @PostMapping("/registration")
    public ResponseEntity<ProfileDTO> registration(@RequestBody ProfileDTO dto,
                                                   @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLanguage appLanguage) {
        log.info("registration", dto.getEmail(), dto.getPhone());
        return ResponseEntity.ok(authService.registration(dto, appLanguage));
    }

   @GetMapping("/verification/email/{id}")
    public ResponseEntity<Boolean> emailVerification(@PathVariable("id") String id) {
        return ResponseEntity.ok(authService.emailVerification(id));
    }
}
