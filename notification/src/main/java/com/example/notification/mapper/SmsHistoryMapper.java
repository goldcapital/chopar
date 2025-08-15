package com.example.notification.mapper;

import com.example.notification.dto.SmsHistoryDto;
import com.example.notification.entity.SmsHistoryEntity;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface SmsHistoryMapper {
    @Mapping(target = "code", source = "code")
    @Mapping(target = "phone", source = "phoneNumber")
    @Mapping(target = "status", source = "smsStatus")
    SmsHistoryEntity toEntity(SmsHistoryDto smsHistoryDto);
}
