package com.example.customer.controller;

import com.example.customer.dto.PageableResult;
import com.example.customer.dto.ProfileDTO;
import com.example.customer.dto.request.ProfileUpdateRequest;
import com.example.customer.enums.AppLanguage;
import com.example.customer.service.ProfileService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;

@RestController("/v1")
@Slf4j
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    /// the admin
    @PostMapping("/create")
    public ResponseEntity<Boolean> create(@RequestBody ProfileDTO dto,
                                          @RequestHeader(value = "Accept-Language", defaultValue = "UZ") String appLanguage) {

        return ResponseEntity.ok(profileService.crete(dto, AppLanguage.valueOf(appLanguage)));

    }

    @PutMapping("/update-any")
    public ResponseEntity<Boolean> updateANY(@RequestBody ProfileUpdateRequest dto,
                                             @RequestHeader(value = "Accept-Language", defaultValue = "UZ") String appLanguage) {
        return ResponseEntity.ok(profileService.updateANY(dto, AppLanguage.valueOf(appLanguage.toUpperCase())));
    }

    @GetMapping("/get-profile-all")
    public ResponseEntity<PageableResult<List<ProfileDTO>>> getProfileAll(@RequestParam(defaultValue = "1") @Min(value = 1, message = "page min 1") Integer page,
                                                                          @RequestParam(defaultValue = "50") @Max(value = 600, message = "max siz 50")
                                                                          @Min(value = 10, message = "siz min 10") Integer size
    ) {
        return ResponseEntity.ok(profileService.getProfileAll(PageRequest.of(page - 1, size)));

    }

    /// admin
    @DeleteMapping("/profile-delete-by/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable("id") Long id,
                                              @RequestHeader(value = "Accept-Language", defaultValue = "UZ") String appLanguage) {
        return ResponseEntity.ok(profileService.deleteById(id, AppLanguage.valueOf(appLanguage.toUpperCase())));
    }
}