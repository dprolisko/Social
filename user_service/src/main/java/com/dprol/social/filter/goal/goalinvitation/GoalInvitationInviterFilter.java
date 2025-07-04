package com.dprol.social.filter.goal.goalinvitation;

import com.dprol.social.dto.goal.GoalInvitationFilterDto;
import com.dprol.social.entity.goal.GoalInvitation;
import com.dprol.social.filter.goal.goalinvitation.GoalInvitationFilter;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component

public class GoalInvitationInviterFilter implements GoalInvitationFilter {

    @Override
    public boolean booleanGoalInvitation(GoalInvitationFilterDto goalInvitationFilterDto) {
        return goalInvitationFilterDto.getInviterId() != null;
    }

    @Override
    public Stream<GoalInvitation> filterGoalInvitations(Stream<GoalInvitation> goalInvitation, GoalInvitationFilterDto goalInvitationFilterDto) {
        return goalInvitation.filter(u->u.getInviter().getId().equals(goalInvitationFilterDto.getInviterId()));
    }
}