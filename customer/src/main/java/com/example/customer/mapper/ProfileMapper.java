package com.example.customer.mapper;


import com.example.customer.dto.ProfileDTO;
import com.example.customer.dto.request.CustomerRequest;
import com.example.customer.dto.request.ProfileUpdateRequest;
import com.example.customer.entity.ProfileEntity;
import com.example.customer.enums.ProfileRole;
import com.example.customer.enums.ProfileStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    @Mapping(target = "password", source = "encode")
    @Mapping(target = "status", source = "profileStatus")
    @Mapping(target = "keycloakId",source = "keycloakId")
    @Mapping(target = "username",source = "identifier")
    @Mapping(target = "name",source = "dto.lastname")
    @Mapping(target = "role",source = "roleUser")
    ProfileEntity toEntityPhone(String identifier, CustomerRequest dto, String encode, ProfileStatus profileStatus, ProfileRole roleUser, String keycloakId);

    ProfileDTO toDto(ProfileEntity profileEntity);
    @Mapping(target = "id",ignore = true)
    void update(@MappingTarget ProfileEntity profileEntity, ProfileUpdateRequest dto);
}
