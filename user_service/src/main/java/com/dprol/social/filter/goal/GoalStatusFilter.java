package com.dprol.social.filter.goal;

import com.dprol.social.dto.goal.GoalFilterDto;
import com.dprol.social.entity.goal.Goal;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component

public class GoalStatusFilter implements GoalFilter {
    @Override
    public boolean booleanFilter(GoalFilterDto goalFilterDto) {
        return goalFilterDto.getGoalStatus() != null;
    }

    @Override
    public Stream<Goal> filterGoals(Stream<Goal> goals, GoalFilterDto goalFilterDto) {
        return goals.filter(u -> u.getStatus().equals(goalFilterDto.getGoalStatus()));
    }
}
