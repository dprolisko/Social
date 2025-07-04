package com.dprol.social.validator.goal.goalinvitation;

public interface GoalInvitationValidator {

    void validateInvitation(Long goalInvitationId);

    void validateInviter(Long inviterId);

    void validateInvited(Long invitedId);
}
