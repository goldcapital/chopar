package com.example.customer.service;


import com.example.customer.dto.PageableResult;
import com.example.customer.dto.ProfileDTO;
import com.example.customer.dto.request.ProfileUpdateRequest;
import com.example.customer.entity.ProfileEntity;
import com.example.customer.enums.AppLanguage;
import com.example.customer.enums.ProfileStatus;
import com.example.customer.exp.AppBadException;
import com.example.customer.mapper.ProfileMapper;
import com.example.customer.repository.ProfileRepository;
import com.example.customer.util.MDUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.example.customer.config.ThrowIfMessage.ITEM_NOT_FOUND;


public interface ProfileService {

     Boolean crete(ProfileDTO dto, AppLanguage appLanguage) ;

     PageableResult<List<ProfileDTO>> getProfileAll(PageRequest of);

     Boolean updateANY(ProfileUpdateRequest dto, AppLanguage appLanguage);

    Boolean deleteById(Long id,AppLanguage language);
}
