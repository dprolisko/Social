package com.dprol.achievement_service.mapper;

import com.dprol.achievement_service.dto.AchievementProgressDto;
import com.dprol.achievement_service.entity.AchievementProgress;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AchievementProgressMapper {
    AchievementProgress toEntity(AchievementProgressDto achievementProgressDto);
    AchievementProgressDto toDto(AchievementProgress achievementProgress);
}
