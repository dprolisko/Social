package com.dprol.social.validator;

import com.dprol.social.entity.goal.Goal;
import com.dprol.social.entity.goal.GoalStatus;
import com.dprol.social.entity.user.User;
import com.dprol.social.exception.DataValidationException;
import com.dprol.social.repository.goal.GoalRepository;
import com.dprol.social.validator.goal.GoalValidatorImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GoalValidatorImplTest {

    @Mock
    private GoalRepository goalRepository;

    @InjectMocks
    private GoalValidatorImpl validator;

    private final Long validGoalId = 1L;
    private final Long invalidGoalId = 999L;
    private User testUser;
    private Goal activeGoal = new Goal(1L, "Active Goal", GoalStatus.active);
    private Goal completedGoal = new Goal(2L, "Completed Goal", GoalStatus.completed);

    // validateGoalById tests
    @Test
    void validateGoalById_ShouldNotThrow_WhenGoalExists() {
        when(goalRepository.existsById(validGoalId)).thenReturn(true);
        assertDoesNotThrow(() -> validator.validateGoalById(validGoalId));
    }

    @Test
    void validateGoalById_ShouldThrow_WhenGoalNotExists() {
        when(goalRepository.existsById(invalidGoalId)).thenReturn(false);

        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> validator.validateGoalById(invalidGoalId)
        );

        assertEquals("Goal with id 999 does not exist", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(longs = {0, -1})
    void validateGoalById_ShouldHandleInvalidIds(Long id) {
        when(goalRepository.existsById(id)).thenReturn(false);

        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> validator.validateGoalById(id)
        );

        assertEquals("Goal with id " + id + " does not exist", exception.getMessage());
    }

    // validateGoalNotCompleted tests
    @ParameterizedTest
    @EnumSource(value = GoalStatus.class, names = {"active", "planned", "completed"})
    void validateGoalNotCompleted_ShouldNotThrow_WhenNotCompleted(GoalStatus status) {
        Goal goal = new Goal(3L, "Test Goal", status);
        assertDoesNotThrow(() -> validator.validateGoalNotCompleted(goal));
    }

    @Test
    void validateGoalNotCompleted_ShouldThrow_WhenCompleted() {
        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> validator.validateGoalNotCompleted(completedGoal)
        );

        assertEquals("Goal is already completed", exception.getMessage());
    }

    @Test
    void validateGoalNotCompleted_ShouldThrow_ForNullGoal() {
        assertThrows(
                NullPointerException.class,
                () -> validator.validateGoalNotCompleted(null)
        );
    }
}