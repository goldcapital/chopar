package com.example.notification.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.validation.annotation.Validated;

import java.lang.reflect.Type;
@Validated
@UtilityClass
public class Utils {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public <T> T parseObject(String value, ParameterizedTypeReference<T> valueType) {
        TypeReference<T> tTypeReference = getType(valueType);
        try {
            return objectMapper.readValue(value, tTypeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
    public <T> T parseObject(String value, Class<T> clazz) {
        try {
            return objectMapper.readValue(value, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    public static <T> TypeReference<T> getType(ParameterizedTypeReference<T> type) {
        return new TypeReference<>() {
            @Override
            public Type getType() {
                return type.getType();
            }
        };
    }
}

