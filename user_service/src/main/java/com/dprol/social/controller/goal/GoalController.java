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
@RequestMapping("/goal")

public class GoalController {

    private final GoalService goalService;

    private UserContextConfig userContextConfig;

    @PostMapping("/create/{userId}")
    public GoalDto createGoal(GoalDto goalDto, @PathVariable Long userId){
        return goalService.createGoal(goalDto, userId);
    }

    @GetMapping("/get/{userId}")
    public List<GoalDto> getListGoals(@PathVariable Long userId, GoalFilterDto goalFilterDto){
        return goalService.getListGoals(userId, goalFilterDto);
    }

    @GetMapping("/find/{goalId}")
    public Goal findGoalById(@PathVariable Long goalId){
        return goalService.findGoalById(goalId);
    }

    @PutMapping("/update/{goalId}")
    public GoalDto updateGoal(GoalDto goalDto, @PathVariable Long goalId){
        long userId = userContextConfig.getUserId();
        return goalService.updateGoal(goalDto, userId, goalId);
    }
}
