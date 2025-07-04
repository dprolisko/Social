package com.dprol.social.controller.goal;

import com.dprol.social.dto.goal.GoalInvitationDto;
import com.dprol.social.dto.goal.GoalInvitationFilterDto;
import com.dprol.social.entity.goal.GoalInvitation;
import com.dprol.social.service.goal.goalinvitation.GoalInvitationService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("GoalInvitation")

public class GoalInvitationController {

    private final GoalInvitationService goalInvitationService;

    @PostMapping
    public GoalInvitationDto invite(GoalInvitationDto goalInvitationDto, Long userId){
        return goalInvitationService.invite(goalInvitationDto, userId);
    }

    @PostMapping
    public GoalInvitationDto acceptInvitation(GoalInvitationDto goalInvitationDto, Long userId){
        return goalInvitationService.acceptInvitation(goalInvitationDto, userId);
    }

    @GetMapping
    public List<GoalInvitationDto> getInvitationsList(Long goalInvitationId, GoalInvitationFilterDto goalInvitationFilterDto){
        return goalInvitationService.getInvitationsList(goalInvitationId, goalInvitationFilterDto);
    }

    @GetMapping
    public List<GoalInvitationDto> getInvitedList(Long invitedId, GoalInvitationFilterDto goalInvitationFilterDto){
        return goalInvitationService.getInvitedList(invitedId, goalInvitationFilterDto);
    }

    @GetMapping
    public GoalInvitation findInvitationById(Long goalInvitationId){
        return goalInvitationService.findInvitationById(goalInvitationId);
    }
}
