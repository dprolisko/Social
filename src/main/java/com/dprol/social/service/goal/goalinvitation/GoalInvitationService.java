package com.dprol.social.service.goal.goalinvitation;

import com.dprol.social.dto.goal.GoalInvitationDto;
import com.dprol.social.dto.goal.GoalInvitationFilterDto;

import java.util.List;

public interface GoalInvitationService {

    GoalInvitationDto invite(GoalInvitationDto goalInvitationDto, Long userId);

    GoalInvitationDto acceptInvitation(GoalInvitationDto goalInvitationDto, Long userId);

    void deleteInvitation(Long goalInvitationId);

    List<GoalInvitationDto> getInvitationsList(Long goalInvitationId, GoalInvitationFilterDto goalInvitationFilterDto);

    List<GoalInvitationDto> getInvitedList(Long invitedId, GoalInvitationFilterDto goalInvitationFilterDto);
}
