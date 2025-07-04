package com.dprol.social.controller.goal;

import com.dprol.social.config.UserContextConfig;
import com.dprol.social.dto.goal.GoalDto;
import com.dprol.social.dto.goal.GoalFilterDto;
import com.dprol.social.entity.goal.Goal;
import com.dprol.social.service.goal.goal.GoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("Goal")

public class GoalController {

    private final GoalService goalService;

    private UserContextConfig userContextConfig;

    @PostMapping
    public GoalDto createGoal(GoalDto goalDto, Long userId){
        return goalService.createGoal(goalDto, userId);
    }

    @GetMapping
    public List<GoalDto> getListGoals(Long userId, GoalFilterDto goalFilterDto){
        return goalService.getListGoals(userId, goalFilterDto);
    }

    @GetMapping
    public Goal findGoalById(Long goalId){
        return goalService.findGoalById(goalId);
    }

    @PutMapping
    public GoalDto updateGoal(GoalDto goalDto, Long goalId){
        Long userId = userContextConfig.getUserId();
        return goalService.updateGoal(goalDto, userId, goalId);
    }
}
