package com.example.chopar_1.service;

import com.example.chopar_1.dto.ProfileDTO;
import com.example.chopar_1.entity.ProfileEntity;
import com.example.chopar_1.entity.mapper.ProfileMapper;
import com.example.chopar_1.enums.AppLanguage;
import com.example.chopar_1.exp.AppBadException;
import com.example.chopar_1.repository.ProfileRepository;
import com.example.chopar_1.util.SpringSecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private  final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;
    private final ResourceBundleService resourceBundleService;
    private final SmsHistoryService smsHistoryService;

    public Boolean crete(ProfileDTO dto, AppLanguage appLanguage) {

        profileRepository.findByEmailOrPhone(dto.getEmail(), dto.getPhone())
                .ifPresent(profile -> {
                    throw new AppBadException(resourceBundleService.getMessage("profile.exists", appLanguage));
                });

        ProfileEntity profileEntity=profileMapper.toEntity(dto);
        profileRepository.save(profileEntity);
        return true;

    }

    public ProfileDTO updateANY(ProfileDTO dto, AppLanguage appLanguage) {
       ProfileEntity profileEntity=profileRepository.findByEmailOrPhone(SpringSecurityUtil.getCurrentUser().getEmail(), SpringSecurityUtil.getCurrentUser().getPhone())
                .orElseThrow(()->new AppBadException(resourceBundleService.getMessage("",appLanguage)));

    /*   if (dto.getPhone()!=null){
           if (smsHistoryService.updateByPhone(dto.getPhone())){

           }
       }*/
        if (dto.getName()!=null){
            profileEntity.setName(dto.getName());
        }
        return null;
    }
}
