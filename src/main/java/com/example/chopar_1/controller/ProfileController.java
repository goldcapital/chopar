package com.example.chopar_1.controller;

import com.example.chopar_1.dto.ProfileDTO;
import com.example.chopar_1.enums.AppLanguage;
import com.example.chopar_1.service.ProfileService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/profile")
@Slf4j
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping("/create")
    public ResponseEntity<Boolean> create(@RequestBody ProfileDTO dto,
                                          @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLanguage appLanguage) {

        return ResponseEntity.ok(profileService.crete(dto, appLanguage));

    }

    @PutMapping("/update-any")
    public ResponseEntity<ProfileDTO> updateANY(@RequestBody ProfileDTO dto,
                                                @RequestHeader(value = "Accept-Language", defaultValue = "uz") AppLanguage appLanguage) {
        return ResponseEntity.ok(profileService.updateANY(dto,appLanguage));
    }
}