package com.dprol.analytic_service.listener;

import com.dprol.analytic_service.entity.Analytic;
import com.dprol.analytic_service.entity.Type;
import com.dprol.analytic_service.event.GoalCompletedEvent;
import com.dprol.analytic_service.mapper.GoalCompletedMapper;
import com.dprol.analytic_service.service.AnalyticService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class GoalCompletedListener extends Listener<GoalCompletedEvent> {

    private final AnalyticService analyticService;

    private final GoalCompletedMapper goalCompletedMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, GoalCompletedEvent.class, event -> {
            Analytic analytic = goalCompletedMapper.toAnalytic(event);
            analytic.setType(Type.POST_VIEW);
            analyticService.saveAnalytic(analytic);
        });
    }
}
