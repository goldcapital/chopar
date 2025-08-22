package com.example.region.service;

import com.example.region.dto.request.RegionCreatRequest;
import com.example.region.dto.response.RegionResponse;
import com.example.region.dto.response.RegionResponseAll;
import com.example.region.enums.AppLanguage;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface RegionService {
    RegionResponse creatRegion(RegionCreatRequest request, AppLanguage language);

    RegionResponse update(Long id, @Valid RegionCreatRequest request, AppLanguage appLanguage);

    Boolean deleteById(Long id, AppLanguage appLanguage);

    List<RegionResponse> getAllRegion(PageRequest pageable);

    List<RegionResponseAll> getAllLang(PageRequest of, AppLanguage appLanguage);
}
