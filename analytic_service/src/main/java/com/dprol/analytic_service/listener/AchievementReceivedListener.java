package com.dprol.analytic_service.listener;

import com.dprol.analytic_service.entity.Analytic;
import com.dprol.analytic_service.entity.Type;
import com.dprol.analytic_service.event.AchievementReceivedEvent;
import com.dprol.analytic_service.mapper.AchievementReceivedMapper;
import com.dprol.analytic_service.service.AnalyticService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component

public class AchievementReceivedListener extends Listener<AchievementReceivedEvent>{

    private final AnalyticService analyticService;

    private final AchievementReceivedMapper achievementReceivedMapper;

    public AchievementReceivedListener(ObjectMapper objectMapper, AnalyticService analyticService, AchievementReceivedMapper achievementReceivedMapper) {
        super(objectMapper);

        this.analyticService = analyticService;
        this.achievementReceivedMapper = achievementReceivedMapper;
    }

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        handleEvent(message, AchievementReceivedEvent.class, event -> {
            Analytic analytic = achievementReceivedMapper.toAnalytic(event);
            analytic.setType(Type.ACHIEVEMENT_RECEIVED);
            analyticService.saveAnalytic(analytic);
        });
    }
}
