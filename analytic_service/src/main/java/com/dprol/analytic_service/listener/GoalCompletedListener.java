package com.dprol.analytic_service.listener;

import com.dprol.analytic_service.entity.Analytic;
import com.dprol.analytic_service.entity.Type;
import com.dprol.analytic_service.event.GoalCompletedEvent;
import com.dprol.analytic_service.mapper.GoalCompletedMapper;
import com.dprol.analytic_service.service.AnalyticService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component

public class GoalCompletedListener extends Listener<GoalCompletedEvent> {

    private final AnalyticService analyticService;

    private final GoalCompletedMapper goalCompletedMapper;

    public GoalCompletedListener(ObjectMapper objectMapper, AnalyticService analyticService, GoalCompletedMapper goalCompletedMapper) {
        super(objectMapper);
        this.analyticService = analyticService;
        this.goalCompletedMapper = goalCompletedMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, GoalCompletedEvent.class, event -> {
            Analytic analytic = goalCompletedMapper.toAnalytic(event);
            analytic.setType(Type.POST_VIEW);
            analyticService.saveAnalytic(analytic);
        });
    }
}
