package com.example.region.service;

import com.example.region.enums.AppLanguage;

public interface ResourceBundleService {
     String getMessage(String code, AppLanguage appLanguage);

}
