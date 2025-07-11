package com.dprol.social.validator;

import com.dprol.social.exception.DataValidationException;
import com.dprol.social.repository.goal.GoalInvitationRepository;
import com.dprol.social.validator.goal.goalinvitation.GoalInvitationValidatorImpl;
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
class GoalInvitationValidatorImplTest {

    @Mock
    private GoalInvitationRepository goalInvitationRepository;

    @InjectMocks
    private GoalInvitationValidatorImpl validator;

    private final Long validId = 1L;
    private final Long invalidId = 999L;

    // validateInvitation tests
    @Test
    void validateInvitation_ShouldNotThrow_WhenInvitationExists() {
        when(goalInvitationRepository.existsById(validId)).thenReturn(true);
        assertDoesNotThrow(() -> validator.validateInvitation(validId));
    }

    @Test
    void validateInvitation_ShouldThrow_WhenInvitationNotExists() {
        when(goalInvitationRepository.existsById(invalidId)).thenReturn(false);

        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> validator.validateInvitation(invalidId)
        );

        assertEquals("Inviation 999 does not exist", exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {0L, -1L})
    void validateInvitation_ShouldHandleInvalidIds(Long id) {
        if (id == null) {
            DataValidationException exception = assertThrows(
                    DataValidationException.class,
                    () -> validator.validateInvitation(null)
            );
            assertEquals("Inviation null does not exist", exception.getMessage());
        } else {
            when(goalInvitationRepository.existsById(id)).thenReturn(false);

            DataValidationException exception = assertThrows(
                    DataValidationException.class,
                    () -> validator.validateInvitation(id)
            );
            assertEquals("Inviation " + id + " does not exist", exception.getMessage());
        }
    }

    // validateInviter tests
    @Test
    void validateInviter_ShouldNotThrow_WhenInviterExists() {
        when(goalInvitationRepository.existsById(validId)).thenReturn(true);
        assertDoesNotThrow(() -> validator.validateInviter(validId));
    }

    @Test
    void validateInviter_ShouldThrow_WhenInviterNotExists() {
        when(goalInvitationRepository.existsById(invalidId)).thenReturn(false);

        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> validator.validateInviter(invalidId)
        );

        assertEquals("Inviter 999 does not exist", exception.getMessage());
    }

    // validateInvited tests
    @Test
    void validateInvited_ShouldNotThrow_WhenInvitedExists() {
        when(goalInvitationRepository.existsById(validId)).thenReturn(true);
        assertDoesNotThrow(() -> validator.validateInvited(validId));
    }

    @Test
    void validateInvited_ShouldThrow_WhenInvitedNotExists() {
        when(goalInvitationRepository.existsById(invalidId)).thenReturn(false);

        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> validator.validateInvited(invalidId)
        );

        // Внимание: в оригинале опечатка - "Inviter" вместо "Invited"
        assertEquals("Inviter 999 does not exist", exception.getMessage());
    }

    // Тестирование сообщений об ошибках
    @Test
    void validateInvited_ErrorMessage_ShouldBeCorrect() {
        Long testId = 123L;
        when(goalInvitationRepository.existsById(testId)).thenReturn(false);

        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> validator.validateInvited(testId)
        );

        // Проверяем фактическое сообщение
        assertEquals("Inviter 123 does not exist", exception.getMessage());
    }

    // Тестирование вызова репозитория
    @Test
    void validateInvitation_ShouldCallRepositoryWithCorrectId() {
        Long testId = 555L;
        when(goalInvitationRepository.existsById(testId)).thenReturn(true);

        validator.validateInvitation(testId);

        verify(goalInvitationRepository).existsById(testId);
    }

    // Тестирование нескольких вызовов
    @Test
    void multipleValidations_ShouldWorkCorrectly() {
        when(goalInvitationRepository.existsById(1L)).thenReturn(true);
        when(goalInvitationRepository.existsById(2L)).thenReturn(false);

        assertDoesNotThrow(() -> validator.validateInvitation(1L));

        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> validator.validateInvitation(2L)
        );

        assertEquals("Inviation 2 does not exist", exception.getMessage());
    }
}