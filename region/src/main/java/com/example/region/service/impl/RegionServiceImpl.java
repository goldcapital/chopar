package com.example.region.service.impl;

import com.example.region.dto.request.RegionCreatRequest;
import com.example.region.dto.response.RegionResponse;
import com.example.region.dto.response.RegionResponseAll;
import com.example.region.entity.RegionEntity;
import com.example.region.enums.AppLanguage;
import com.example.region.exp.AppBadException;
import com.example.region.mapper.RegionMapper;
import com.example.region.repository.RegionRep;
import com.example.region.service.RegionService;
import com.example.region.service.ResourceBundleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.BiConsumer;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegionServiceImpl implements RegionService {
    private final RegionRep regionRep;
    private final RegionMapper regionMapper;
    private final ResourceBundleService resourceBundleService;
    @Override
    public RegionResponse creatRegion(RegionCreatRequest request, AppLanguage appLanguage) {
        validateRegionNameUniqueness(request, appLanguage);
        var entity = regionRep.save(regionMapper.toEntity(request));
        return regionMapper.toDto(entity);
    }

    @Override
    public RegionResponse update(Long id, RegionCreatRequest request, AppLanguage appLanguage) {
        return regionRep.findByIdAndVisible(id, Boolean.TRUE)
                .map(region -> {
                    log.info("Region entity {} by Id {} ", region, id);

                    regionMapper.update(region, request);
                    log.info("Region updated entity {} by Id {} ", region, id);

                    var entity = regionRep.save(region);
                    return regionMapper.toDto(entity);
                }).orElseThrow(() -> {
                    log.error("Region not found by Id {} ", id);
                    return new AppBadException(resourceBundleService.getMessage("item.not.found", appLanguage));
                });


    }

    @Override
    public Boolean deleteById(Long id, AppLanguage appLanguage) {
        return regionRep.findByIdAndVisible(id, Boolean.TRUE).map(region -> {
            log.info("Region delete entity {} by Id {} ", region, id);
            region.setVisible(Boolean.FALSE);
            regionRep.save(region);
            return Boolean.TRUE;
        }).orElseThrow(() -> new AppBadException(resourceBundleService.getMessage("item.not.found", appLanguage)));

    }

    @Override
    public List<RegionResponse> getAllRegion(PageRequest pageable) {
        return regionRep.findAllByVisible(pageable, Boolean.TRUE).stream()
                .map(regionMapper::toDto).toList();
    }

    @Override
    public List<RegionResponseAll> getAllLang(PageRequest of, AppLanguage appLanguage) {
        return this.regionRep.findAllByVisible(of, Boolean.TRUE).stream()
                .map(regionEntity -> {
            var name = getNameLang(appLanguage, regionEntity);
            return this.toDto(regionEntity.getId(), name);
        }).toList();
    }


private void validateRegionNameUniqueness(RegionCreatRequest request, AppLanguage language) {
    BiConsumer<Boolean, String> throwIf = (condition, messageKey) -> {
        if (condition) {
            throw new AppBadException(resourceBundleService.getMessage(messageKey, language));
        }
    };
    throwIf.accept(regionRep.existsByNameUZAndVisible(request.nameUz(), Boolean.TRUE), "");
    throwIf.accept(regionRep.existsByNameRuAndVisible(request.nameUz(), Boolean.TRUE), "");
    throwIf.accept(regionRep.existsByNameEnAndVisible(request.nameEn(), Boolean.TRUE), "");
}
    private String getNameLang(AppLanguage appLanguage, RegionEntity region) {
        return switch (appLanguage) {
            case UZ -> region.getNameUZ();
            case RU -> region.getNameRu();
            case EN -> region.getNameEn();
        };
    }

    private RegionResponseAll toDto(Long id, String nameUZ) {
        return new RegionResponseAll(id, nameUZ);
    }
}
