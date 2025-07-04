package com.dprol.social.service.goal.filter;

import com.dprol.social.dto.goal.GoalInvitationFilterDto;
import com.dprol.social.entity.goal.GoalInvitation;

import java.util.stream.Stream;

public interface GoalInvitationFilterService {

    Stream<GoalInvitation> filterGoalsInvitation(Stream<GoalInvitation> goalInvitation, GoalInvitationFilterDto goalInvitationFilterDto);

    Stream<GoalInvitation> filterGoalsInvited(Stream<GoalInvitation> goalInvitation, GoalInvitationFilterDto goalInvitationFilterDto);
}
