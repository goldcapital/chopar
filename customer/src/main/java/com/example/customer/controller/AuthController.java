package com.example.customer.controller;

import com.example.customer.dto.ProfileDTO;
import com.example.customer.dto.request.CustomerRequest;
import com.example.customer.dto.request.ProfileLoginRequestDTO;
import com.example.customer.dto.response.AuthTokenResponse;
import com.example.customer.enums.AppLanguage;
import com.example.customer.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@Tag(name = "Authorization Api list", description = "Api list for Authorization")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/loge")
    @Operation(summary = "Api for login", description = "this api used for authorization")
    public ResponseEntity<AuthTokenResponse> loge(@RequestBody ProfileLoginRequestDTO dto,
                                                  @RequestHeader(value = "Accept-Language", defaultValue = "UZ") String appLanguage) {
        return ResponseEntity.ok(authService.loge(dto, AppLanguage.valueOf(appLanguage.toUpperCase())));
    }

    @Operation(summary = "Api for email  registration", description = "this api used for registration email")
    @PostMapping("/registration/email")
    public ResponseEntity<Boolean> registrationEmail(@RequestBody CustomerRequest dto,
                                                     @RequestHeader(value = "Accept-Language", defaultValue = "UZ") String appLanguage) {
        log.info("registration data {}", dto);
        return ResponseEntity.ok(authService.registration(dto, AppLanguage.valueOf(appLanguage.toUpperCase())));
    }

    @Operation(summary = "Api for email code Verification", description = "this api used for verification email")
    @GetMapping("/verification/email/{id}")
    public ResponseEntity<Boolean> emailVerification(@PathVariable("id") String id) {
        log.info("verification data {}", id);
        return ResponseEntity.ok(authService.emailVerification(id));
    }

    @Operation(summary = "Api for Phone code  Verification", description = "this api used for verification phone")
    @PostMapping("/verification/phone")
    public ResponseEntity<Boolean> smsVerification(@NotBlank @RequestParam("code") String code,
                                                   @NotBlank @RequestParam("phone") String phone,
                                                   @RequestHeader(value = "Accept-Language", defaultValue = "UZ") String appLanguage) {
        return ResponseEntity.ok(authService.smsVerification(phone, code, AppLanguage.valueOf(appLanguage.toUpperCase())));
    }
}
