package com.dprol.social.validator.goal;

import com.dprol.social.entity.goal.Goal;
import com.dprol.social.entity.goal.GoalStatus;
import com.dprol.social.entity.user.User;
import com.dprol.social.exception.DataValidationException;
import com.dprol.social.repository.goal.GoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component

public class GoalValidatorImpl implements GoalValidator {

    private final int MAX_GOALS_AMOUNT = 10;

    private final GoalRepository goalRepository;

    @Override
    public void validateGoalById(Long goalId) {
        boolean isExist = goalRepository.existsById(goalId);
        if (!isExist) {
            throw new DataValidationException("Goal with id " + goalId + " does not exist");
        }
    }

    @Override
    public void validateGoalByAmountUsers(User user) {
        if (user.getGoals().size()>=MAX_GOALS_AMOUNT) {
            throw new DataValidationException("Goal amount exceeds maximum amount of goals");
        }
    }

    @Override
    public void validateGoalNotCompleted(Goal goal) {
        if (goal.getStatus().equals(GoalStatus.completed)){
            throw new DataValidationException("Goal is already completed");
        }
    }
}
