package com.dprol.achievement_service.dto;

import com.dprol.achievement_service.entity.Rarity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder

public class AchievementDto {

    private Long id;

    private String title;

    private String description;

    private Rarity rarity;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
