package com.dprol.social.filter.goal;

import com.dprol.social.dto.goal.GoalFilterDto;
import com.dprol.social.entity.goal.Goal;

import java.util.stream.Stream;

public interface GoalFilter {

    boolean booleanFilter(GoalFilterDto goalFilterDto);

    Stream<Goal> filterGoals(Stream<Goal> goals, GoalFilterDto goalFilterDto);
}
