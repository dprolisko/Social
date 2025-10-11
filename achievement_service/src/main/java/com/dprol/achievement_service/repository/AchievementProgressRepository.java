package com.dprol.achievement_service.repository;

import com.dprol.achievement_service.entity.AchievementProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AchievementProgressRepository extends JpaRepository<AchievementProgress, Long> {

    Optional<AchievementProgress> findByUserIdAndAchievementId(Long userId, Long achievementId);

    List<AchievementProgress> findByUserId(Long userId);
}
