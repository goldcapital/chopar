package com.example.category.controller;


import com.example.category.dto.PageableResult;
import com.example.category.dto.reponse.CategoryResponse;
import com.example.category.dto.reponse.CategoryResponseAll;
import com.example.category.dto.request.CategoryCreatRequest;
import com.example.category.enums.AppLanguage;
import com.example.category.service.CategoryService;
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
public class CategoryController {
    private final CategoryService categoryService;

    /// admin
    @PostMapping("/creat")
    public ResponseEntity<CategoryResponse> creatRegion(@Valid CategoryCreatRequest request,
                                                        @RequestHeader(value = "Accept-Language", defaultValue = "UZ") String appLanguage) {
        return ResponseEntity.ok(categoryService.creatRegion(request, AppLanguage.valueOf(appLanguage.toUpperCase())));
    }

    //admin
    @PostMapping("/update-by{id}")
    public ResponseEntity<CategoryResponse> update(@PathVariable(name = "id") Long id,
                                                 @Valid CategoryCreatRequest request,
                                                 @RequestHeader(value = "Accept-Language", defaultValue = "UZ") String appLanguage) {
        return ResponseEntity.ok(categoryService.update(id, request, AppLanguage.valueOf(appLanguage.toUpperCase())));
    }

    //admin
    @DeleteMapping("/delete-by/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable(name = "id") Long id,
                                              @RequestHeader(value = "Accept-Language", defaultValue = "UZ") String appLanguage) {
        return ResponseEntity.ok(categoryService.deleteById(id, AppLanguage.valueOf(appLanguage.toUpperCase())));
    }

    //admin
    @GetMapping("/get-all")
    public ResponseEntity<PageableResult<List<CategoryResponse>>> getAllRegion(
            @Min(value = 1, message = "page min 1")
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @Min(value = 10, message = "size min 10")
            @Max(value = 50, message = "size max 50")
            @RequestParam(value = "size", defaultValue = "50") Integer size) {
        return ResponseEntity.ok(categoryService.getAllRegion(PageRequest.of(page - 1, size)));
    }

    @GetMapping("/get-all-by-lang")
    public ResponseEntity<List<CategoryResponseAll>> getAllRegionByLang(
            @Min(value = 1, message = "page min 1")
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @Min(value = 10, message = "size min 10")
            @Max(value = 50, message = "size max 50")
            @RequestParam(value = "size", defaultValue = "50") Integer size,
            @RequestHeader(value = "Accept-Language", defaultValue = "UZ") String appLanguage
    ) {
        return ResponseEntity.ok(categoryService.getAllLang(PageRequest.of(page - 1, size), AppLanguage.valueOf(appLanguage.toUpperCase())));
    }
}
