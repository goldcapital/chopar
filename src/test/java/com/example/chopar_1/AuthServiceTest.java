package com.example.chopar_1;

import com.example.chopar_1.dto.JwtDTO;
import com.example.chopar_1.dto.ProfileDTO;
import com.example.chopar_1.dto.request.ProfileLoginRequestDTO;
import com.example.chopar_1.entity.ProfileEntity;
import com.example.chopar_1.enums.AppLanguage;
import com.example.chopar_1.enums.ProfileStatus;
import com.example.chopar_1.exp.AppBadException;
import com.example.chopar_1.repository.ProfileRepository;
import com.example.chopar_1.service.AuthService;
import com.example.chopar_1.util.MDUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthServiceTest {
    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegistrationPhone_Success() {
        ProfileDTO dto = new ProfileDTO();
        dto.setName("John Doe");
        dto.setPhone("1234567890");
        dto.setPassword("password");

        when(profileRepository.findByEmailOrPhone(any(), any())).thenReturn(Optional.empty());
        when(profileRepository.save(any(ProfileEntity.class))).thenReturn(new ProfileEntity());

        assertDoesNotThrow(() -> authService.registrationPhone(dto, AppLanguage.en));
    }

    @Test
    public void testRegistrationPhone_ProfileAlreadyExists() {
        ProfileDTO dto = new ProfileDTO();
        dto.setEmail("john@example.com");
        dto.setPhone("1234567890");

        ProfileEntity existingProfile = new ProfileEntity();
        existingProfile.setStatus(ProfileStatus.ACTIVE);
        when(profileRepository.findByEmailOrPhone(dto.getEmail(), dto.getPhone())).thenReturn(Optional.of(existingProfile));

        AppBadException exception = assertThrows(AppBadException.class, () -> {
            authService.registrationPhone(dto, AppLanguage.en);
        });

        assertEquals("This.email.has.been.registered", exception.getMessage());
    }

    @Test
    public void testEmailVerification_Success() {
        String token = "valid-token";
        JwtDTO jwtDTO = new JwtDTO();
        jwtDTO.setEmail("john@example.com");
        jwtDTO.setAppLanguage(AppLanguage.en);

        when(profileRepository.getId(jwtDTO.getEmail())).thenReturn(Optional.of(new ProfileEntity()));

        assertDoesNotThrow(() -> authService.emailVerification(token));
    }

    @Test
    public void testEmailVerification_ProfileNotFound() {
        String token = "invalid-token";

        when(profileRepository.getId(any())).thenReturn(Optional.empty());

        AppBadException exception = assertThrows(AppBadException.class, () -> {
            authService.emailVerification(token);
        });

        assertEquals("item.not.found", exception.getMessage());
    }

    @Test
    public void testLoge_Success() {
        ProfileLoginRequestDTO dto = new ProfileLoginRequestDTO();
        dto.setEmail("john@example.com");
        dto.setPassword("password");

        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setStatus(ProfileStatus.ACTIVE);
        profileEntity.setPassword(MDUtil.encode(dto.getPassword()));
        when(profileRepository.findByEmailOrPhone(dto.getEmail(), null)).thenReturn(Optional.of(profileEntity));

        ProfileDTO result = authService.loge(dto, AppLanguage.en);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
    }
}