package com.example.customer.service.impl;

import com.example.customer.entity.SmsHistoryEntity;
import com.example.customer.enums.AppLanguage;
import com.example.customer.exp.AppBadException;
import com.example.customer.repository.SmsRepository;
import com.example.customer.service.ResourceBundleService;
import com.example.customer.service.SmsServerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.example.customer.config.ThrowIfMessage.ITEM_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableCaching
public class SmsServerServiceImpl implements SmsServerService {
    private final SmsRepository smsRepository;
    private final ResourceBundleService messageSource;

    @Override
    @Cacheable(cacheNames = "sms",key = "#phoneNumber")
    public SmsHistoryEntity findByPhoneNumber(String phoneNumber, AppLanguage appLanguage) {
        return smsRepository.findByPhone(phoneNumber).orElseThrow(()->
                new AppBadException(messageSource.getMessage(ITEM_NOT_FOUND,appLanguage)));
    }


    @Override
    public Long getCountSendSms(String phone, LocalDateTime from, LocalDateTime to) {
        return smsRepository.countSendSms(phone, from, to);
    }
}
