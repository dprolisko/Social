package com.dprol.social.validator.goal.goalinvitation;

import com.dprol.social.entity.goal.GoalInvitation;

public interface GoalInvitationValidator {

    void validateInvitation(Long goalInvitationId);

    void validateInviter(Long inviterId);

    void validateInvited(Long invitedId);
}
