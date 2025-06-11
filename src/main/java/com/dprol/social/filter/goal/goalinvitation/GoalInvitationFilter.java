package com.dprol.social.filter.goal.goalinvitation;

import com.dprol.social.dto.goal.GoalInvitationDto;
import com.dprol.social.dto.goal.GoalInvitationFilterDto;
import com.dprol.social.entity.goal.GoalInvitation;

import java.util.stream.Stream;

public interface GoalInvitationFilter {

    boolean booleanGoalInvitation(GoalInvitationFilterDto goalInvitationFilterDto);

    Stream<GoalInvitation> filterGoalInvitations(Stream<GoalInvitation> goalInvitation, GoalInvitationFilterDto goalInvitationFilterDto);
}
