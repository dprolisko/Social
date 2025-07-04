package com.dprol.social.validator.goal;

import com.dprol.social.entity.goal.Goal;
import com.dprol.social.entity.user.User;

public interface GoalValidator {

    void validateGoalById(Long goalId);

    void validateGoalByAmountUsers(User user);

    void validateGoalNotCompleted(Goal goal);
}
