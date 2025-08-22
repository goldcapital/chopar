package com.example.region.mapper;

import com.example.region.dto.request.RegionCreatRequest;
import com.example.region.dto.response.RegionResponse;
import com.example.region.entity.RegionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RegionMapper {
    @Mapping(target = "nameUZ", source = "nameUz")
    RegionEntity toEntity(RegionCreatRequest request);

    @Mapping(target = "nameUz", source = "nameUZ")
    RegionResponse toDto(RegionEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nameUZ", source = "nameUz")
    void update(@MappingTarget RegionEntity region, RegionCreatRequest request);
}
