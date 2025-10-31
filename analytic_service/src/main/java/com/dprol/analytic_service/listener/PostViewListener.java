package com.dprol.analytic_service.listener;

import com.dprol.analytic_service.entity.Analytic;
import com.dprol.analytic_service.entity.Type;
import com.dprol.analytic_service.event.PostViewEvent;
import com.dprol.analytic_service.mapper.PostViewMapper;
import com.dprol.analytic_service.service.AnalyticService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component

public class PostViewListener extends Listener<PostViewEvent> {

    private final AnalyticService analyticService;

    private final PostViewMapper postViewMapper;

    public PostViewListener(ObjectMapper objectMapper, AnalyticService analyticService, PostViewMapper postViewMapper) {
        super(objectMapper);
        this.analyticService = analyticService;
        this.postViewMapper = postViewMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, PostViewEvent.class, event -> {
            Analytic analytic = postViewMapper.toAnalytic(event);
            analytic.setType(Type.POST_VIEW);
            analyticService.saveAnalytic(analytic);
        });
    }
}
