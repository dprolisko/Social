package com.dprol.social.service.goal.goalinvitation;

import com.dprol.social.dto.goal.GoalInvitationDto;
import com.dprol.social.dto.goal.GoalInvitationFilterDto;
import com.dprol.social.entity.goal.GoalInvitation;
import com.dprol.social.entity.user.User;
import com.dprol.social.exception.GoalInvitationNotFoundException;
import com.dprol.social.exception.UserNotFoundException;
import com.dprol.social.mapper.goal.GoalInvitationMapper;
import com.dprol.social.repository.UserRepository;
import com.dprol.social.repository.goal.GoalInvitationRepository;
import com.dprol.social.service.goal.filter.GoalInvitationFilterService;
import com.dprol.social.validator.goal.goalinvitation.GoalInvitationValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor

public class GoalInvitationServiceImpl implements GoalInvitationService {

    private final GoalInvitationRepository goalInvitationRepository;

    private final GoalInvitationMapper goalInvitationMapper;

    private final GoalInvitationFilterService goalInvitationFilterService;

    private final GoalInvitationValidator goalInvitationValidator;

    private final UserRepository userRepository;

    @Override
    public GoalInvitationDto invite(GoalInvitationDto goalInvitationDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        goalInvitationValidator.validateInvitation(userId);
        GoalInvitation goalInvitation = goalInvitationMapper.toEntity(goalInvitationDto);
        goalInvitationRepository.save(goalInvitation);
        return goalInvitationMapper.toDto(goalInvitation);
    }

    @Override
    public GoalInvitationDto acceptInvitation(GoalInvitationDto goalInvitationDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        goalInvitationValidator.validateInvited(userId);
        GoalInvitation invited = goalInvitationMapper.toEntity(goalInvitationDto);
        goalInvitationRepository.save(invited);
        return goalInvitationMapper.toDto(invited);
    }

    @Override
    public void deleteInvitation(Long goalInvitationId) {
        goalInvitationRepository.deleteById(goalInvitationId);
    }

    @Override
    public List<GoalInvitationDto> getInvitationsList(Long goalInvitationId, GoalInvitationFilterDto goalInvitationFilterDto) {
        goalInvitationValidator.validateInvitation(goalInvitationId);
        Stream<GoalInvitation> filterGoalInvitation = goalInvitationRepository.findByGoalInvitationStream(goalInvitationId);
        return goalInvitationFilterService.filterGoalsInvitation(filterGoalInvitation, goalInvitationFilterDto).map(goalInvitationMapper::toDto).toList();
    }

    @Override
    public List<GoalInvitationDto> getInvitedList(Long invitedId, GoalInvitationFilterDto goalInvitationFilterDto) {
        goalInvitationValidator.validateInvited(invitedId);
        Stream<GoalInvitation> filterGoalInvitation = goalInvitationRepository.findByGoalInvitationStream(invitedId);
        return goalInvitationFilterService.filterGoalsInvited(filterGoalInvitation, goalInvitationFilterDto).map(goalInvitationMapper::toDto).toList();
    }

    @Override
    public GoalInvitation findInvitationById(Long goalInvitationId) {
        return goalInvitationRepository.findById(goalInvitationId).orElseThrow(() -> new GoalInvitationNotFoundException("Invitation not found"));
    }
}