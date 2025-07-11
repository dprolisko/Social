package com.dprol.social.validator;

import com.dprol.social.dto.PremiumDto;
import com.dprol.social.exception.DataValidationException;
import com.dprol.social.repository.PremiumRepository;
import com.dprol.social.validator.premium.PremiumValidatorImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PremiumValidatorImplTest {

    @Mock
    private PremiumRepository premiumRepository;

    @InjectMocks
    private PremiumValidatorImpl validator;

    private final Long validPremiumId = 1L;
    private final Long invalidPremiumId = 999L;

    // Тест успешной валидации
    @Test
    void validatePremium_ShouldNotThrow_WhenPremiumExists() {
        PremiumDto dto = new PremiumDto(validPremiumId, "active");
        when(premiumRepository.existsById(validPremiumId)).thenReturn(true);

        assertDoesNotThrow(() -> validator.validatePremium(dto));
    }

    // Тест исключения при отсутствии премиума
    @Test
    void validatePremium_ShouldThrow_WhenPremiumNotExists() {
        PremiumDto dto = new PremiumDto(invalidPremiumId, "ACTIVE");
        when(premiumRepository.existsById(invalidPremiumId)).thenReturn(false);

        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> validator.validatePremium(dto)
        );

        assertEquals("There is no premium", exception.getMessage());
    }

    // Тест обработки null DTO
    @Test
    void validatePremium_ShouldThrow_WhenDtoIsNull() {
        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> validator.validatePremium(null)
        );

        assertEquals("Premium DTO cannot be null", exception.getMessage());
    }

    // Тест обработки null ID в DTO
    @Test
    void validatePremium_ShouldThrow_WhenPremiumIdIsNull() {
        PremiumDto dto = new PremiumDto(null, "ACTIVE");

        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> validator.validatePremium(dto)
        );

        assertEquals("Premium ID cannot be null", exception.getMessage());
    }

    // Тест различных статусов премиума
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " ", "EXPIRED", "ACTIVE", "PENDING"})
    void validatePremium_ShouldCheckOnlyId_RegardlessOfStatus(String status) {
        PremiumDto dto = new PremiumDto(validPremiumId, status);
        when(premiumRepository.existsById(validPremiumId)).thenReturn(true);

        assertDoesNotThrow(() -> validator.validatePremium(dto));
    }

    // Тест отрицательных ID
    @Test
    void validatePremium_ShouldHandleNegativeId() {
        Long negativeId = -1L;
        PremiumDto dto = new PremiumDto(negativeId, "ACTIVE");
        when(premiumRepository.existsById(negativeId)).thenReturn(false);

        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> validator.validatePremium(dto)
        );

        assertEquals("There is no premium", exception.getMessage());
    }

    // Тест пограничных значений
    @Test
    void validatePremium_ShouldHandleZeroId() {
        Long zeroId = 0L;
        PremiumDto dto = new PremiumDto(zeroId, "ACTIVE");
        when(premiumRepository.existsById(zeroId)).thenReturn(false);

        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> validator.validatePremium(dto)
        );

        assertEquals("There is no premium", exception.getMessage());
    }

    // Тест максимального значения Long
    @Test
    void validatePremium_ShouldHandleMaxLongId() {
        Long maxId = Long.MAX_VALUE;
        PremiumDto dto = new PremiumDto(maxId, "ACTIVE");
        when(premiumRepository.existsById(maxId)).thenReturn(true);

        assertDoesNotThrow(() -> validator.validatePremium(dto));
    }

    // Тест вызова репозитория с правильным ID
    @Test
    void validatePremium_ShouldCallRepositoryWithCorrectId() {
        PremiumDto dto = new PremiumDto(validPremiumId, "ACTIVE");
        when(premiumRepository.existsById(validPremiumId)).thenReturn(true);

        validator.validatePremium(dto);

        verify(premiumRepository).existsById(validPremiumId);
    }
}