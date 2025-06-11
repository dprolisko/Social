package com.dprol.social.service.goal.filter;

import com.dprol.social.dto.goal.GoalInvitationFilterDto;
import com.dprol.social.entity.goal.GoalInvitation;
import com.dprol.social.filter.goal.goalinvitation.GoalInvitationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor

public class GoalInvitationFilterServiceImpl implements GoalInvitationFilterService {
    private final List<GoalInvitationFilter> goalInvitationFilters;

    @Override
    public Stream<GoalInvitation> filterGoalsInvitation(Stream<GoalInvitation> goalInvitation, GoalInvitationFilterDto goalInvitationFilterDto) {
        if(goalInvitationFilterDto != null) {
            for(GoalInvitationFilter goalInvitationFilter : goalInvitationFilters) {
                if (goalInvitationFilter.booleanGoalInvitation(goalInvitationFilterDto)) {
                    goalInvitation = goalInvitationFilter.filterGoalInvitations(goalInvitation, goalInvitationFilterDto);
                }
            }
        }
        return goalInvitation;
    }

    @Override
    public Stream<GoalInvitation> filterGoalsInvited(Stream<GoalInvitation> goalInvitation, GoalInvitationFilterDto goalInvitationFilterDto) {
        if (goalInvitationFilterDto != null) {
            for (GoalInvitationFilter goalInvitationFilter : goalInvitationFilters) {
                if (goalInvitationFilter.booleanGoalInvitation(goalInvitationFilterDto)) {
                    goalInvitation = goalInvitationFilter.filterGoalInvitations(goalInvitation, goalInvitationFilterDto);
                }
            }
        }
        return goalInvitation;
    }
}
