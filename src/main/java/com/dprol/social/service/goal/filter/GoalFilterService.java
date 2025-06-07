package com.dprol.social.service.goal.filter;

import com.dprol.social.dto.goal.GoalFilterDto;
import com.dprol.social.entity.goal.Goal;

import java.util.stream.Stream;

public interface GoalFilterService {

    Stream<Goal> filterGoals(Stream<Goal> goals, GoalFilterDto goalFilterDto);
}
