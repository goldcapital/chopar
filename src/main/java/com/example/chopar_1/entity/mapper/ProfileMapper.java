package com.example.chopar_1.entity.mapper;

import com.example.chopar_1.dto.ProfileDTO;
import com.example.chopar_1.entity.ProfileEntity;
import com.example.chopar_1.enums.AppLanguage;
import com.example.chopar_1.util.JWTUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface ProfileMapper extends EntityMapper<ProfileEntity, ProfileDTO> {


}
