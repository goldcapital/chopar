package com.example.category.mapper;

import com.example.category.dto.reponse.CategoryResponse;
import com.example.category.dto.request.CategoryCreatRequest;
import com.example.category.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryEntity toEntity(CategoryCreatRequest request);

    CategoryResponse toDto(CategoryEntity categoryEntity);

    void update(@MappingTarget CategoryEntity categoryEntity, CategoryCreatRequest request);
}
