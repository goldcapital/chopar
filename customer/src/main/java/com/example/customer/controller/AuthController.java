package com.example.customer.controller;

import com.example.customer.dto.ProfileDTO;
import com.example.customer.dto.request.CustomerRequestPhone;
import com.example.customer.dto.request.ProfileLoginRequestDTO;
import com.example.customer.dto.response.SmsResponse;
import com.example.customer.enums.AppLanguage;
import com.example.customer.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/auth")
@Tag(name = "Authorization Api list",description = "Api list for Authorization")
@RequiredArgsConstructor
public class AuthController {

    private AuthService authService;

    @GetMapping("/loge")
    @Operation(summary = "Api for login", description = "this api used for authorization")
    public ResponseEntity<ProfileDTO>loge(@RequestBody ProfileLoginRequestDTO dto,
                                          @RequestHeader(value = "Accept-Language",defaultValue = "uz") AppLanguage appLanguage){
       return ResponseEntity.ok(authService.loge(dto,appLanguage));
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
    public ResponseEntity<SmsResponse> registrationPhone(@RequestBody  CustomerRequestPhone dto,
                                                         @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLanguage appLanguage) {
        log.info("registration Phone {}",dto.phone());

        return ResponseEntity.ok(authService.registrationPhone(dto, appLanguage));
    }
    @Operation(summary = "Api for email code Verification",description = "this api used for verification email")
   @GetMapping("/verification/email/{id}")
    public ResponseEntity<Boolean> emailVerification(@PathVariable("id") String id) {
        return ResponseEntity.ok(authService.emailVerification(id));
    }

    @Operation(summary = "Api for Phone code  Verification",description = "this api used for verification phone")
    @PostMapping("/verification/phone")
    public ResponseEntity<Boolean> smsVerification(@RequestParam("code") String code,
                                                   @RequestParam("phone") String phone,
                                                   @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLanguage appLanguage) {
        return ResponseEntity.ok(authService.smsVerification(phone,code, appLanguage));
    }
}
