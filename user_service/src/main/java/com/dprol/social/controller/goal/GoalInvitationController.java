package com.dprol.social.controller.goal;

import com.dprol.social.dto.goal.GoalInvitationDto;
import com.dprol.social.dto.goal.GoalInvitationFilterDto;
import com.dprol.social.entity.goal.GoalInvitation;
import com.dprol.social.service.goal.goalinvitation.GoalInvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("goalInvitation")

public class GoalInvitationController {

    private final GoalInvitationService goalInvitationService;

    @PostMapping("/invite")
    public GoalInvitationDto invite(GoalInvitationDto goalInvitationDto, Long userId){
        return goalInvitationService.invite(goalInvitationDto, userId);
    }

    @PostMapping("/accept")
    public GoalInvitationDto acceptInvitation(GoalInvitationDto goalInvitationDto, Long userId){
        return goalInvitationService.acceptInvitation(goalInvitationDto, userId);
    }

    @GetMapping("/getInvitationsList")
    public List<GoalInvitationDto> getInvitationsList(Long goalInvitationId, GoalInvitationFilterDto goalInvitationFilterDto){
        return goalInvitationService.getInvitationsList(goalInvitationId, goalInvitationFilterDto);
    }

    @GetMapping("/getInvitedList")
    public List<GoalInvitationDto> getInvitedList(Long invitedId, GoalInvitationFilterDto goalInvitationFilterDto){
        return goalInvitationService.getInvitedList(invitedId, goalInvitationFilterDto);
    }

    @GetMapping("/find/{goalInvitationId}")
    public GoalInvitation findInvitationById(Long goalInvitationId){
        return goalInvitationService.findInvitationById(goalInvitationId);
    }
}
