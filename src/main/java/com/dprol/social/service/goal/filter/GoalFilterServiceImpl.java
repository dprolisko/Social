package com.dprol.social.service.goal.filter;

import com.dprol.social.dto.goal.GoalFilterDto;
import com.dprol.social.dto.user.UserFilterDto;
import com.dprol.social.entity.goal.Goal;
import com.dprol.social.entity.user.User;
import com.dprol.social.filter.goal.GoalFilter;
import com.dprol.social.filter.user.UserFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor

public class GoalFilterServiceImpl implements GoalFilterService {
    private final List<GoalFilter> goalFilters;

    @Override
    public Stream<Goal> filterGoals(Stream<Goal> goals, GoalFilterDto goalFilterDto) {
        if (goalFilterDto != null){
            for (GoalFilter goalFilter : goalFilters){
                if(goalFilter.booleanFilter(goalFilterDto)){
                    goals = goalFilter.filterGoals(goals, goalFilterDto);
                }
            }
        }
        return goals;
    }
}
