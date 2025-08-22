package com.example.category.service;

import com.example.category.dto.PageableResult;
import com.example.category.dto.reponse.CategoryResponse;
import com.example.category.dto.reponse.CategoryResponseAll;
import com.example.category.dto.request.CategoryCreatRequest;
import com.example.category.enums.AppLanguage;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface CategoryService {
    CategoryResponse creatRegion( CategoryCreatRequest request, AppLanguage appLanguage);

    CategoryResponse update(Long id,  CategoryCreatRequest request, AppLanguage appLanguage);

    Boolean deleteById(Long id, AppLanguage appLanguage);

    PageableResult<List<CategoryResponse>> getAllRegion(PageRequest pageable);

    List<CategoryResponseAll> getAllLang(PageRequest of, AppLanguage appLanguage);
    CategoryResponse getById(Long id, AppLanguage appLanguage);
}
