package com.example.fie_service.exp;


import com.example.fie_service.enums.Errors;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FileException extends RuntimeException {
    private final Errors errors;


}
