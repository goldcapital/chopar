package com.example.category.service.impl;


import com.example.category.enums.AppLanguage;
import com.example.category.service.ResourceBundleService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResourceBundleServiceImpl implements ResourceBundleService {
    private final ResourceBundleMessageSource messageSource;
    @Override
    public String getMessage(String code, AppLanguage appLanguage) {
        return "";
    }
}
