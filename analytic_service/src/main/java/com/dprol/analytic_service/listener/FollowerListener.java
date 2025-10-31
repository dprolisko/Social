package com.dprol.analytic_service.listener;

import com.dprol.analytic_service.entity.Analytic;
import com.dprol.analytic_service.entity.Type;
import com.dprol.analytic_service.event.FollowerEvent;
import com.dprol.analytic_service.mapper.FollowerMapper;
import com.dprol.analytic_service.service.AnalyticService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component

public class FollowerListener extends Listener<FollowerEvent> {

    private final AnalyticService analyticService;

    private final FollowerMapper followerMapper;


    public FollowerListener(AnalyticService analyticService, FollowerMapper followerMapper, ObjectMapper objectMapper) {
        super(objectMapper);
        this.analyticService = analyticService;
        this.followerMapper = followerMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, FollowerEvent.class, event -> {
            Analytic analytic = followerMapper.toAnalytic(event);
            analytic.setType(Type.POST_VIEW);
            analyticService.saveAnalytic(analytic);
        });
    }
}
