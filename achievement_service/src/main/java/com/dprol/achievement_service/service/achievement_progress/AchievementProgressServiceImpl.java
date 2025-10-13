package com.dprol.achievement_service.service.achievement_progress;

import com.dprol.achievement_service.dto.AchievementProgressDto;
import com.dprol.achievement_service.entity.Achievement;
import com.dprol.achievement_service.entity.AchievementProgress;
import com.dprol.achievement_service.exception.NotFoundException;
import com.dprol.achievement_service.mapper.AchievementProgressMapper;
import com.dprol.achievement_service.repository.AchievementProgressRepository;
import com.dprol.achievement_service.repository.AchievementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class AchievementProgressServiceImpl implements AchievementProgressService {

    private final AchievementProgressRepository achievementProgressRepository;

    private final AchievementProgressMapper achievementProgressMapper;

    @Override
    public void createAchievementProgress(Long userId, Long achievementId) {
        achievementProgressRepository.createProgress(userId, achievementId);
    }

    @Override
    public AchievementProgressDto getAchievementProgress(Long userId, Long achievementId) {
        AchievementProgress achievementProgress = achievementProgressRepository.findByUserIdAndAchievementId(userId, achievementId).orElseThrow(()-> new NotFoundException("Not found"));
        return achievementProgressMapper.toDto(achievementProgress);
    }

    @Override
    public List<AchievementProgressDto> getAllAchievementProgress(Long userId) {
        return achievementProgressRepository.findByUserId(userId).stream().map(achievementProgressMapper::toDto).collect(Collectors.toList());
    }
}
