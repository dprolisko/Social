package com.dprol.social.validator;

import com.dprol.social.dto.SubscriptionDto;
import com.dprol.social.exception.DataValidationException;
import com.dprol.social.repository.SubscriptionRepository;
import com.dprol.social.validator.subscription.SubscriptionValidatorImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionValidatorImplTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @InjectMocks
    private SubscriptionValidatorImpl validator;

    private final SubscriptionDto validDto = new SubscriptionDto(1L, 2L);
    private final SubscriptionDto sameIdsDto = new SubscriptionDto(1L, 1L);
    private final SubscriptionDto existingSubscriptionDto = new SubscriptionDto(3L, 4L);

    // validateFollowerAndFollowingId tests
    @Test
    void validateFollowerAndFollowingId_ShouldNotThrow_WhenDifferentIds() {
        assertDoesNotThrow(() -> validator.validateFollowerAndFollowingId(validDto));
    }

    @Test
    void validateFollowerAndFollowingId_ShouldThrow_WhenSameIds() {
        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> validator.validateFollowerAndFollowingId(sameIdsDto)
        );

        assertEquals("Follower and following has same ids", exception.getMessage());
    }

    @Test
    void validateFollowerAndFollowingId_ShouldThrow_WhenDtoIsNull() {
        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> validator.validateFollowerAndFollowingId(null)
        );

        assertEquals("Subscription DTO cannot be null", exception.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "1, null",
            "null, 1",
            "null, null"
    })
    void validateFollowerAndFollowingId_ShouldHandleNullIds(Long followerId, Long followingId) {
        SubscriptionDto dto = new SubscriptionDto(followerId, followingId);

        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> validator.validateFollowerAndFollowingId(dto)
        );

        assertTrue(exception.getMessage().contains("Follower and following has same ids") ||
                exception.getMessage().contains("Subscription DTO cannot be null"));
    }

    // validateSubscription tests
    @Test
    void validateSubscription_ShouldNotThrow_WhenSubscriptionNotExists() {
        when(subscriptionRepository.existsByFollowerId(
                existingSubscriptionDto.getFollowerId(),
                existingSubscriptionDto.getFollowingId()
        )).thenReturn(false);

        assertDoesNotThrow(() -> validator.validateSubscription(existingSubscriptionDto));
    }

    @Test
    void validateSubscription_ShouldThrow_WhenSubscriptionExists() {
        when(subscriptionRepository.existsByFollowerId(
                existingSubscriptionDto.getFollowerId(),
                existingSubscriptionDto.getFollowingId()
        )).thenReturn(true);

        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> validator.validateSubscription(existingSubscriptionDto)
        );

        assertEquals("Subscription with id 3 already exists", exception.getMessage());
    }

    @Test
    void validateSubscription_ShouldThrow_WhenDtoIsNull() {
        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> validator.validateSubscription(null)
        );

        assertEquals("Subscription DTO cannot be null", exception.getMessage());
    }

    @Test
    void validateSubscription_ShouldCallRepositoryWithCorrectParameters() {
        when(subscriptionRepository.existsByFollowerId(1L, 2L)).thenReturn(false);

        validator.validateSubscription(validDto);

        verify(subscriptionRepository).existsByFollowerId(1L, 2L);
    }

    @Test
    void validateSubscription_ShouldHandleNegativeIds() {
        SubscriptionDto negativeIdsDto = new SubscriptionDto(-1L, -2L);
        when(subscriptionRepository.existsByFollowerId(-1L, -2L)).thenReturn(true);

        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> validator.validateSubscription(negativeIdsDto)
        );

        assertEquals("Subscription with id -1 already exists", exception.getMessage());
    }

    // Комбинированный тест
    @Test
    void allValidations_ShouldPassForValidSubscription() {
        // Проверка разных ID
        assertDoesNotThrow(() -> validator.validateFollowerAndFollowingId(validDto));

        // Проверка отсутствия подписки
        when(subscriptionRepository.existsByFollowerId(1L, 2L)).thenReturn(false);
        assertDoesNotThrow(() -> validator.validateSubscription(validDto));
    }

    // Тест порядка валидации
    @Test
    void shouldValidateIdsBeforeExistence() {
        // Попытка проверить подписку с одинаковыми ID
        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> {
                    validator.validateFollowerAndFollowingId(sameIdsDto);
                    validator.validateSubscription(sameIdsDto);
                }
        );

        // Должны получить ошибку на первом этапе
        assertEquals("Follower and following has same ids", exception.getMessage());
        // Репозиторий не должен вызываться
        verifyNoInteractions(subscriptionRepository);
    }
}