package com.example.category.service;


import com.example.category.enums.AppLanguage;

public interface ResourceBundleService {
     String getMessage(String code, AppLanguage appLanguage);

}
