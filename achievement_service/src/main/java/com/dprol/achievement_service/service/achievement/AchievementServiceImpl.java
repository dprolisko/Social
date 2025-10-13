package com.dprol.achievement_service.service.achievement;

import com.dprol.achievement_service.dto.AchievementDto;
import com.dprol.achievement_service.entity.Achievement;
import com.dprol.achievement_service.entity.AchievementProgress;
import com.dprol.achievement_service.entity.User;
import com.dprol.achievement_service.exception.NotFoundException;
import com.dprol.achievement_service.mapper.AchievementMapper;
import com.dprol.achievement_service.repository.AchievementProgressRepository;
import com.dprol.achievement_service.repository.AchievementRepository;
import com.dprol.achievement_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class AchievementServiceImpl implements AchievementService {

    private final AchievementRepository achievementRepository;

    private final AchievementMapper achievementMapper;

    private final UserRepository userRepository;

    private final AchievementProgressRepository achievementProgressRepository;

    @Override
    public void giveAchievement(Long userId, Achievement achievement) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User Not Found"));
        user.builder().userId(userId).achievement(achievement).build();
        userRepository.save(user);
    }

    @Override
    public boolean hasAchievement(Long userId, Long achievementId) {
        return achievementRepository.existsById(achievementId);
    }

    @Override
    public AchievementProgress getAchievementProgress(Long userId, Long achievementId) {
        var userAndAchievemnt =  achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId).orElseThrow(() -> new NotFoundException("User And Achievement Not Found"));
        return userAndAchievemnt;
    }

    @Override
    public void createProgress(Long userId, Long achievementId) {
        achievementProgressRepository.createProgress(userId, achievementId);
    }

    @Override
    public AchievementDto findAchievementById(Long achievementId) {
        Achievement achievement = achievementRepository.findById(achievementId).orElseThrow(() -> new NotFoundException("Achievement Not Found"));
        return achievementMapper.toDto(achievement);
    }

    @Override
    public AchievementDto findAchievementByTitle(String title) {
        Achievement achievement = achievementRepository.findByAchievementTitle(title).orElseThrow(() -> new NotFoundException("Achievement Not Found"));
        return achievementMapper.toDto(achievement);
    }
}
