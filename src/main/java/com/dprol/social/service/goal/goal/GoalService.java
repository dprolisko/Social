package com.dprol.social.service.goal.goal;

import com.dprol.social.dto.goal.GoalDto;
import com.dprol.social.dto.goal.GoalFilterDto;
import com.dprol.social.entity.goal.Goal;

import java.util.List;

public interface GoalService {

    GoalDto createGoal(GoalDto goalDto, Long userId);

    void deleteGoal(Long goalId);

    List<GoalDto> getListGoals(Long userId, GoalFilterDto goalFilterDto);

    Goal findGoalById(Long goalId);

    GoalDto updateGoal(GoalDto goalDto, Long userId, Long goalId);
}
