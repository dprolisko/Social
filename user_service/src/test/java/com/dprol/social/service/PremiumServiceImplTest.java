package com.dprol.social.service;

import com.dprol.social.dto.PremiumDto;
import com.dprol.social.entity.Premium;
import com.dprol.social.event.PremiumBuyEvent;
import com.dprol.social.exception.PremiumNotFoundException;
import com.dprol.social.mapper.PremiumMapper;
import com.dprol.social.publisher.PremiumBuyPublisher;
import com.dprol.social.repository.PremiumRepository;
import com.dprol.social.service.premium.PremiumServiceImpl;
import com.dprol.social.validator.premium.PremiumValidator;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PremiumServiceImplTest {

    @Mock
    private PremiumRepository premiumRepository;

    @Mock
    private PremiumMapper premiumMapper;

    @Mock
    private PremiumValidator premiumValidator;

    @Mock
    private PremiumBuyPublisher premiumBuyPublisher;

    @InjectMocks
    private PremiumServiceImpl premiumService;

    // Тестовые данные
    private final Long USER_ID = 1L;
    private final Long PREMIUM_ID = 10L;
    private PremiumDto premiumDto;
    private Premium premiumEntity;
    private PremiumBuyEvent event;

    // ------------------------ activatePremium() ------------------------

    @Test
    void activatePremium_SuccessWithEvent() {
        // Подготовка
        when(premiumMapper.toEntity(USER_ID, premiumDto)).thenReturn(premiumEntity);
        //when(premiumMapper.toDto(premiumEntity)).thenReturn(premiumDto);

        // Действие
        PremiumDto result = premiumService.activatePremium(USER_ID, premiumDto);

        // Проверка
        assertNotNull(result);
        assertEquals(PREMIUM_ID, result.getPremiumId());
        verify(premiumBuyPublisher).publisher(argThat(e ->
                e.getPremiumId().equals(PREMIUM_ID) &&
                        e.getUserId().equals(USER_ID)
        ));
        verify(premiumRepository).save(premiumEntity);
    }

    @Test
    void activatePremium_IdMismatch_NoEventPublished() {
        // Подготовка
        Premium differentPremium = new Premium();
        when(premiumMapper.toEntity(USER_ID, premiumDto)).thenReturn(differentPremium);
        //when(premiumMapper.toDto(differentPremium)).thenReturn(premiumDto);

        // Действие
        PremiumDto result = premiumService.activatePremium(USER_ID, premiumDto);

        // Проверка
        assertNotNull(result);
        verify(premiumBuyPublisher, never()).publisher(any());
        verify(premiumRepository).save(differentPremium);
    }

    @Test
    void activatePremium_PublisherThrows_StillSaves() {
        // Подготовка
        when(premiumMapper.toEntity(USER_ID, premiumDto)).thenReturn(premiumEntity);
        //when(premiumMapper.toDto(premiumEntity)).thenReturn(premiumDto);
        doThrow(new RuntimeException("Queue error")).when(premiumBuyPublisher).publisher(any());

        // Действие
        PremiumDto result = premiumService.activatePremium(USER_ID, premiumDto);

        // Проверка
        assertNotNull(result);
        verify(premiumRepository).save(premiumEntity);
    }

    // ------------------------ deactivatePremium() ------------------------

    @Test
    void deactivatePremium_Success() {
        // Подготовка
        when(premiumRepository.findById(PREMIUM_ID)).thenReturn(Optional.of(premiumEntity));

        // Действие
        premiumService.deactivatePremium(PREMIUM_ID);

        // Проверка
        verify(premiumRepository).delete(premiumEntity);
    }

    @Test
    void deactivatePremium_NotFound() {
        // Подготовка
        when(premiumRepository.findById(PREMIUM_ID)).thenReturn(Optional.empty());

        // Действие + Проверка
        assertThrows(PremiumNotFoundException.class,
                () -> premiumService.deactivatePremium(PREMIUM_ID));

        verify(premiumRepository, never()).delete(any());
    }

    // ------------------------ validatePremium() ------------------------

    @Test
    void validatePremium_Success() {
        // Подготовка
        doNothing().when(premiumValidator).validatePremium(premiumDto);

        // Действие
        premiumService.validatePremium(premiumDto);

        // Проверка
        verify(premiumValidator).validatePremium(premiumDto);
    }

    @Test
    void validatePremium_Failure() {
        // Подготовка
        doThrow(new ValidationException("Invalid premium")).when(premiumValidator).validatePremium(premiumDto);

        // Действие + Проверка
        assertThrows(ValidationException.class,
                () -> premiumService.validatePremium(premiumDto));
    }

    // ------------------------ deletePremium() ------------------------

    @Test
    void deletePremium_WithExpiredItems() {
        // Подготовка
        List<Long> expiredIds = List.of(1L, 2L, 3L);
        when(premiumRepository.findAllExpiredId()).thenReturn(expiredIds);

        // Действие
        premiumService.deletePremium();

        // Проверка
        verify(premiumRepository).deleteAllById(expiredIds);
    }

    @Test
    void deletePremium_NoExpiredItems() {
        // Подготовка
        when(premiumRepository.findAllExpiredId()).thenReturn(Collections.emptyList());

        // Действие
        premiumService.deletePremium();

        // Проверка
        verify(premiumRepository, never()).deleteAllById(any());
    }

    @Test
    void deletePremium_RepositoryThrows() {
        // Подготовка
        when(premiumRepository.findAllExpiredId()).thenReturn(List.of(1L, 2L));
        doThrow(new RuntimeException("DB error")).when(premiumRepository).deleteAllById(any());

        // Действие + Проверка
        assertThrows(RuntimeException.class,
                () -> premiumService.deletePremium());
    }

    // ------------------------ findPremiumById() ------------------------

    @Test
    void findPremiumById_Success() {
        // Подготовка
        when(premiumRepository.findById(PREMIUM_ID)).thenReturn(Optional.of(premiumEntity));

        // Действие
        Premium result = premiumService.findPremiumById(PREMIUM_ID);

        // Проверка
        assertNotNull(result);
        assertEquals(PREMIUM_ID, result.getId());
    }

    @Test
    void findPremiumById_NotFound() {
        // Подготовка
        when(premiumRepository.findById(PREMIUM_ID)).thenReturn(Optional.empty());

        // Действие + Проверка
        PremiumNotFoundException ex = assertThrows(PremiumNotFoundException.class,
                () -> premiumService.findPremiumById(PREMIUM_ID));

        assertTrue(ex.getMessage().contains("Premium Id:" + PREMIUM_ID));
    }
}
