package com.example.customer.service;


import com.example.customer.dto.ProfileDTO;
import com.example.customer.entity.ProfileEntity;
import com.example.customer.enums.AppLanguage;
import com.example.customer.enums.ProfileStatus;
import com.example.customer.exp.AppBadException;
import com.example.customer.mapper.ProfileMapper;
import com.example.customer.repository.ProfileRepository;
import com.example.customer.util.MDUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final ResourceBundleService resourceBundleService;


    public Boolean crete(ProfileDTO dto, AppLanguage appLanguage) {

        profileRepository.findByEmailOrPhone(dto.getEmail(), dto.getPhone())
                .ifPresent(profile -> {
                    throw new AppBadException(resourceBundleService.getMessage("profile.exists", appLanguage));
                });
        var passwordEncoder = MDUtil.encode(dto.getPassword());
        var profileEntity = profileMapper.toEntity(dto, passwordEncoder, ProfileStatus.ACTIVE);

        profileRepository.save(profileEntity);
        return true;

    }

    public ProfileDTO updateANY(ProfileDTO dto, AppLanguage appLanguage) {
        ProfileEntity profileEntity = profileRepository.findByEmailOrPhone(dto.getEmail(), dto.getPhone())
                .orElseThrow(() -> new AppBadException(resourceBundleService.getMessage("", appLanguage)));

        if (dto.getName() != null) {
            profileEntity.setName(dto.getName());
        }
        return null;
    }
}
