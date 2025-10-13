package com.dprol.achievement_service.service.user;

import com.dprol.achievement_service.dto.AchievementDto;
import com.dprol.achievement_service.dto.UserDto;

import java.util.List;

public interface UserService {

    void giveAchievement(Long userId, AchievementDto achievementDto);

    boolean hasAchievement(Long userId, Long achievementId);

    List<UserDto> getAchievementsByUserId(Long userId);
}
