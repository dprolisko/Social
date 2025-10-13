package com.dprol.achievement_service.controller;

import com.dprol.achievement_service.dto.AchievementProgressDto;
import com.dprol.achievement_service.service.achievement_progress.AchievementProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/achievementProgress")
@RequiredArgsConstructor

public class AchievementProgressController {

    private final AchievementProgressService achievementProgressService;

    @GetMapping("/get/{achievementId}")
    public List<AchievementProgressDto> getAchievementProgress(@PathVariable Long achievementId) {
        return achievementProgressService.getAllAchievementProgress(achievementId);
    }
}
