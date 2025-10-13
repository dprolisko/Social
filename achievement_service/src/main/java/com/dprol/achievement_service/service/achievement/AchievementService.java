package com.dprol.achievement_service.service.achievement;

import com.dprol.achievement_service.dto.AchievementDto;
import com.dprol.achievement_service.entity.Achievement;
import com.dprol.achievement_service.entity.AchievementProgress;

public interface AchievementService {

    void giveAchievement(Long userId, Achievement achievement);

    boolean hasAchievement(Long userId, Long achievementId);

    AchievementProgress getAchievementProgress(Long userId, Long achievementId);

    void createProgress(Long userId, Long achievementId);

    AchievementDto findAchievementById(Long achievementId);

    AchievementDto findAchievementByTitle(String title);
}
