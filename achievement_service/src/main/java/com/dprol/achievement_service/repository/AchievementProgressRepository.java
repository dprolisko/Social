package com.dprol.achievement_service.repository;

import com.dprol.achievement_service.entity.AchievementProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AchievementProgressRepository extends JpaRepository<AchievementProgress, Long> {

    @Query("SELECT a FROM AchievementProgress a WHERE a.userId =: userId AND a.achievement.id =: achievementId")
    Optional<AchievementProgress> findByUserIdAndAchievementId(Long userId, Long achievementId);

    List<AchievementProgress> findByUserId(Long userId);

    @Query(nativeQuery = true, value = "INSERT INTO achievement_progress(user_id, achievement_id) VALUES (:userId, :achievementId)")
    void createProgress(Long userId, Long achievementId);
}
