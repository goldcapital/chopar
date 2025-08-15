package com.example.customer.mapper;

import com.example.customer.dto.ProfileDTO;
import com.example.customer.dto.request.CustomerRequestPhone;
import com.example.customer.entity.ProfileEntity;
import com.example.customer.enums.ProfileStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", source = "encode")
    @Mapping(target = "status", source = "profileStatus")
    ProfileEntity toEntity(ProfileDTO dto, String encode, ProfileStatus profileStatus);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", source = "encode")
    @Mapping(target = "status", source = "profileStatus")
    ProfileEntity toEntityPhone(CustomerRequestPhone dto, String encode, ProfileStatus profileStatus);
}
