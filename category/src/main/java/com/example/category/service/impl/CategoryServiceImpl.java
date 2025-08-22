
package com.example.category.service.impl;

import com.example.category.dto.PageableResult;
import com.example.category.dto.reponse.CategoryResponse;
import com.example.category.dto.reponse.CategoryResponseAll;
import com.example.category.dto.request.CategoryCreatRequest;
import com.example.category.entity.CategoryEntity;
import com.example.category.enums.AppLanguage;
import com.example.category.exp.AppBadException;
import com.example.category.mapper.CategoryMapper;
import com.example.category.repository.CategoryRep;
import com.example.category.service.CategoryService;
import com.example.category.service.ResourceBundleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;
    private final CategoryRep categoryRep;
    private final ResourceBundleService resourceBundleService;

    @Override
    public CategoryResponse creatRegion(CategoryCreatRequest request, AppLanguage appLanguage) {
        validateRegionNameUniqueness(request, appLanguage);

        var categoryEntity = categoryRep.save(categoryMapper.toEntity(request));
        log.info("Create category entity: {}", categoryEntity);

        return categoryMapper.toDto(categoryEntity);
    }

    private void validateRegionNameUniqueness(CategoryCreatRequest request, AppLanguage appLanguage) {
        BiConsumer<Boolean, String> throwIf = (condition, messageKey) -> {
            if (condition) {
                throw new ArithmeticException(resourceBundleService.getMessage(messageKey, appLanguage));
            }
        };
        throwIf.accept(categoryRep.existsByNameUzAndVisible(request.nameUz(), Boolean.TRUE), "");
        throwIf.accept(categoryRep.existsByNameRuAndVisible(request.nameRu(), Boolean.TRUE), "");
        throwIf.accept(categoryRep.existsByNameEnAndVisible(request.nameEn(), Boolean.TRUE), "");
    }

    @Override
    public CategoryResponse update(Long id, CategoryCreatRequest request, AppLanguage appLanguage) {
       return categoryRep.findByIdAndVisible(id, Boolean.TRUE)
               .map(categoryEntity -> {
            categoryMapper.update(categoryEntity, request);
            log.info("Update category entity: {}", categoryEntity);
            categoryRep.save(categoryEntity);
            return categoryMapper.toDto(categoryEntity);
        }).orElseThrow(() -> new AppBadException(resourceBundleService.getMessage("item.not.found", appLanguage)));

    }

    @Override
    public Boolean deleteById(Long id, AppLanguage appLanguage) {
        return categoryRep.findByIdAndVisible(id, Boolean.TRUE)
                .map(categoryEntity -> {
                    categoryEntity.setVisible(Boolean.FALSE);
                    categoryRep.save(categoryEntity);
                    log.info("Delete category entity: {}", categoryEntity);
                    return Boolean.TRUE;
                }).orElseThrow(() -> new ArithmeticException(resourceBundleService.getMessage("item.not.found", appLanguage)));

    }

    @Override
    public PageableResult<List<CategoryResponse>> getAllRegion(PageRequest pageable) {
        Page<CategoryEntity> page = categoryRep.findAllAndVisible(pageable, Boolean.TRUE);
        if (page.isEmpty()) {
            return new PageableResult<>(
                    List.of(), 0, 0, 0);

        }
        List<CategoryResponse> list = page
                .map(categoryMapper::toDto)
                .getContent();
        return new PageableResult<>(list, page.getTotalElements(), page.getTotalPages(), page.getNumber());

    }

    @Override
    public List<CategoryResponseAll> getAllLang(PageRequest of, AppLanguage appLanguage) {
        return categoryRep.findAllAndVisible(of, Boolean.TRUE).
                map(categoryEntity -> {
                    var getName = getNameLang(categoryEntity, appLanguage);
                    return toDto(categoryEntity.getId(), getName);
                }).stream().toList();

    }

    @Override
    public CategoryResponse getById(Long id, AppLanguage appLanguage) {
        return categoryRep.findByIdAndVisible(id, Boolean.TRUE)
                .map(categoryMapper::toDto).orElseThrow(() -> new AppBadException(resourceBundleService.getMessage("item.not.found", appLanguage)));

    }

    private CategoryResponseAll toDto(Long id, String getName) {
        return new CategoryResponseAll(id, getName);
    }

    private String getNameLang(CategoryEntity categoryEntity, AppLanguage appLanguage) {
        return switch (appLanguage) {
            case EN -> categoryEntity.getNameEn();
            case RU -> categoryEntity.getNameRu();
            case UZ -> categoryEntity.getNameUz();
        };
    }

}
