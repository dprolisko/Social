package com.dprol.notification_service.service;

import com.dprol.notification_service.entity.TelegramProfile;
import com.dprol.notification_service.exception.NotFoundException;
import com.dprol.notification_service.repository.TelegramProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TelegramProfileServiceImplTest {

    @Mock
    private TelegramProfileRepository telegramProfileRepository;

    @InjectMocks
    private TelegramProfileServiceImpl telegramProfileService;

    private TelegramProfile profile;

    @BeforeEach
    void setUp() {
        profile = new TelegramProfile();
        profile.setId(1L);
        profile.setUserId(100L);
        profile.setTelegramId(200L);
    }

    @Test
    void saveTelegramProfile_ShouldCallRepositorySave() {
        telegramProfileService.saveTelegramProfile(profile);

        verify(telegramProfileRepository, times(1)).save(profile);
    }

    @Test
    void findByUserId_ShouldReturnProfile_WhenExists() {
        when(telegramProfileRepository.findByUserId(100L)).thenReturn(Optional.of(profile));

        TelegramProfile result = telegramProfileService.findByUserId(100L);

        assertNotNull(result);
        assertEquals(100L, result.getUserId());
        verify(telegramProfileRepository).findByUserId(100L);
    }

    @Test
    void findByUserId_ShouldThrowNotFound_WhenNotExists() {
        when(telegramProfileRepository.findByUserId(999L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> telegramProfileService.findByUserId(999L));
    }

    @Test
    void findByChatId_ShouldReturnOptionalProfile() {
        when(telegramProfileRepository.findByTelegramId(200L)).thenReturn(Optional.of(profile));

        Optional<TelegramProfile> result = telegramProfileService.findByChatId(200L);

        assertTrue(result.isPresent());
        assertEquals(200L, result.get().getTelegramId());
        verify(telegramProfileRepository).findByTelegramId(200L);
    }

    @Test
    void existsByChatId_ShouldReturnTrue_WhenExists() {
        when(telegramProfileRepository.existsByTelegramId(200L)).thenReturn(true);

        boolean exists = telegramProfileService.existsByChatId(200L);

        assertTrue(exists);
        verify(telegramProfileRepository).existsByTelegramId(200L);
    }

    @Test
    void existsByChatId_ShouldReturnFalse_WhenNotExists() {
        when(telegramProfileRepository.existsByTelegramId(300L)).thenReturn(false);

        boolean exists = telegramProfileService.existsByChatId(300L);

        assertFalse(exists);
        verify(telegramProfileRepository).existsByTelegramId(300L);
    }
}
