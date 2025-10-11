package com.dprol.achievement_service.mapper;

import com.dprol.achievement_service.dto.AchievementDto;
import com.dprol.achievement_service.entity.Achievement;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AchievementMapper {
    Achievement toEntity(AchievementDto achievementDto);
    AchievementDto toDto(Achievement achievement);
}
