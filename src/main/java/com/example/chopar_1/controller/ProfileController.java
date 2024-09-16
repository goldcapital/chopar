package com.example.chopar_1.controller;

import com.example.chopar_1.dto.ProfileDTO;
import com.example.chopar_1.service.ProfileService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/profile")
@Slf4j
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;
    @PostMapping("/create")
    public ResponseEntity<Boolean>create(@RequestBody ProfileDTO dto){
        return ResponseEntity.ok(true);
    }
}
