package com.dprol.achievement_service.service;

import com.dprol.achievement_service.dto.AchievementProgressDto;

import java.util.List;

public interface AchievementProgressService {

    void createAchievementProgress(Long userId, Long achievementId);

    AchievementProgressDto getAchievementProgress(Long userId, Long achievementId);

    List<AchievementProgressDto> getAllAchievementProgress(Long userId);
}
