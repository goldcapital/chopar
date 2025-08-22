package com.example.region.controller;

import com.example.region.dto.request.RegionCreatRequest;
import com.example.region.dto.response.RegionResponse;
import com.example.region.dto.response.RegionResponseAll;
import com.example.region.entity.RegionEntity;
import com.example.region.enums.AppLanguage;
import com.example.region.service.RegionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/region")
@RequiredArgsConstructor
public class RegionController {
    private final RegionService regionService;

    /// admin
    @PostMapping("/creat")
    public ResponseEntity<RegionResponse> creatRegion(@Valid RegionCreatRequest request,
                                                      @RequestHeader(value = "Accept-Language", defaultValue = "UZ") String appLanguage) {
        return ResponseEntity.ok(regionService.creatRegion(request, AppLanguage.valueOf(appLanguage.toUpperCase())));
    }

    //admin
    @PostMapping("/update-by{id}")
    public ResponseEntity<RegionResponse> update(@PathVariable(name = "id") Long id,
                                                 @Valid RegionCreatRequest request,
                                                 @RequestHeader(value = "Accept-Language", defaultValue = "UZ") String appLanguage) {
        return ResponseEntity.ok(regionService.update(id, request, AppLanguage.valueOf(appLanguage.toUpperCase())));
    }

    //admin
    @DeleteMapping("/delete-by/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable(name = "id") Long id,
                                              @RequestHeader(value = "Accept-Language", defaultValue = "UZ") String appLanguage) {
        return ResponseEntity.ok(regionService.deleteById(id, AppLanguage.valueOf(appLanguage.toUpperCase())));
    }

    //admin
    @GetMapping("/get-all")
    public ResponseEntity<List<RegionResponse>> getAllRegion(
            @Min(value = 1, message = "page min 1")
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @Min(value = 10, message = "size min 10")
            @Max(value = 50, message = "size max 50")
            @RequestParam(value = "size", defaultValue = "50") Integer size) {
        return ResponseEntity.ok(regionService.getAllRegion(PageRequest.of(page - 1, size)));
    }

    @GetMapping("/get-all-by-lang")
    public ResponseEntity<List<RegionResponseAll>> getAllRegionByLang(
            @Min(value = 1, message = "page min 1")
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @Min(value = 10, message = "size min 10")
            @Max(value = 50, message = "size max 50")
            @RequestParam(value = "size", defaultValue = "50") Integer size,
            @RequestHeader(value = "Accept-Language", defaultValue = "UZ") String appLanguage
    ) {
        return ResponseEntity.ok(regionService.getAllLang(PageRequest.of(page - 1, size), AppLanguage.valueOf(appLanguage.toUpperCase())));
    }
}
