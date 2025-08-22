package com.example.customer.service.impl;

import com.example.customer.dto.PageableResult;
import com.example.customer.dto.ProfileDTO;
import com.example.customer.dto.request.ProfileUpdateRequest;
import com.example.customer.entity.ProfileEntity;
import com.example.customer.enums.AppLanguage;
import com.example.customer.exp.AppBadException;
import com.example.customer.mapper.ProfileMapper;
import com.example.customer.repository.ProfileRepository;
import com.example.customer.service.KeycloakService;
import com.example.customer.service.ProfileService;
import com.example.customer.service.ResourceBundleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.customer.config.ThrowIfMessage.ITEM_NOT_FOUND;
import static com.example.customer.util.ConversionUtils.getUsername;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final ResourceBundleService resourceBundleService;
    private final KeycloakService keycloakService;

    @Override
    public Boolean crete(ProfileDTO dto, AppLanguage appLanguage) {

       /* profileRepository.findByEmailOrPhone(dto.getEmail(), dto.getPhone())
                .ifPresent(profile -> {
                    throw new AppBadException(resourceBundleService.getMessage("profile.exists", appLanguage));
                });
        var passwordEncoder = MDUtil.encode(dto.getPassword());
        var profileEntity = profileMapper.toEntity(dto, passwordEncoder, ProfileStatus.ACTIVE);

        profileRepository.save(profileEntity);*/
        return true;

    }

    @Override
    @Cacheable(value = "profile",
            key = "T(com.example.customer.util.CacheKeyGenerator).generateProfileKeyAll(#of.pageNumber,#of.pageSize)")
    public PageableResult<List<ProfileDTO>> getProfileAll(PageRequest of) {
        doLongRunningTask();
        Page<ProfileEntity> resultPage = profileRepository.findAll(of);

        List<ProfileDTO> content = resultPage
                .map(profileMapper::toDto)
                .getContent();

        return new PageableResult<>(
                content,
                resultPage.getTotalElements(),
                resultPage.getTotalPages(),
                resultPage.getNumber());
    }

    @Override
    @CacheEvict(value = "profile",
            allEntries = true
    )
    public Boolean updateANY(ProfileUpdateRequest dto, AppLanguage appLanguage) {
        var username = getUsername(dto.email(), dto.phone());
        var profileEntity = profileRepository.findByEmailOrPhone(dto.email(), dto.phone()).stream().findAny()
                .orElseThrow(() -> new AppBadException(resourceBundleService.getMessage(ITEM_NOT_FOUND, appLanguage)));

        profileMapper.update(profileEntity, dto);
        log.info("Profile updated successfully DATA {}", profileEntity);
        if (keycloakService.updateKeycloakUser(username, dto, appLanguage)) {
            log.info("update keycloak user success");
            profileRepository.save(profileEntity);
            return true;
        }


        return false;
    }

    @Override
    public Boolean deleteById(Long id, AppLanguage language) {
        return profileRepository.findById(id).map(profileEntity -> {

            var username = getUsername(profileEntity.getEmail(), profileEntity.getPhone());
            keycloakService.deleteByUsername(username, language);
            profileRepository.deleteById(id);
            return Boolean.TRUE;
        }).orElseThrow(() -> new AppBadException(resourceBundleService.getMessage(ITEM_NOT_FOUND, language)));

    }

    private void doLongRunningTask() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
