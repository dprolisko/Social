package com.dprol.achievement_service.controller;

import com.dprol.achievement_service.dto.AchievementDto;
import com.dprol.achievement_service.service.achievement.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/achievement")
@RequiredArgsConstructor

public class AchievementController {

    private final AchievementService achievementService;

    @GetMapping("/get/{achivementId}")
    public AchievementDto getAchievementById(@PathVariable Long achievementId) {
        return achievementService.findAchievementById(achievementId);
    }

    @GetMapping("/getByTitle")
    public AchievementDto getAchievementByTitle(String title) {
        return achievementService.findAchievementByTitle(title);
    }
}
