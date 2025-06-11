package com.dprol.social.validator.goal.goalinvitation;

import com.dprol.social.entity.goal.Goal;
import com.dprol.social.entity.user.User;
import com.dprol.social.exception.DataValidationException;
import com.dprol.social.repository.goal.GoalInvitationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class GoalInvitationValidatorImpl implements GoalInvitationValidator {

    private final GoalInvitationRepository goalInvitationRepository;

    @Override
    public void validateInvitation(Long goalInvitationId) {
        boolean isExist = goalInvitationRepository.existsById(goalInvitationId);
        if (!isExist) {
            throw new DataValidationException("Inviation " + goalInvitationId + " does not exist");
        }
    }

    @Override
    public void validateInviter(Long inviterId) {
        boolean isExist = goalInvitationRepository.existsById(inviterId);
        if (!isExist) {
            throw new DataValidationException("Inviter " + inviterId + " does not exist");
        }
    }

    @Override
    public void validateInvited(Long invitedId) {
        boolean isExist = goalInvitationRepository.existsById(invitedId);
        if (!isExist) {
            throw new DataValidationException("Inviter " + invitedId + " does not exist");
        }
    }
}
