package com.dprol.analytic_service.listener;


import com.dprol.analytic_service.entity.Analytic;
import com.dprol.analytic_service.entity.Type;
import com.dprol.analytic_service.event.PostPublishedEvent;
import com.dprol.analytic_service.mapper.PostPublishedMapper;
import com.dprol.analytic_service.service.AnalyticService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component

public class PostPublishedListener extends Listener<PostPublishedEvent> {

    private final AnalyticService analyticService;

    private final PostPublishedMapper postPublishedMapper;

    public PostPublishedListener(ObjectMapper objectMapper, AnalyticService analyticService, PostPublishedMapper postPublishedMapper) {
        super(objectMapper);
        this.analyticService = analyticService;
        this.postPublishedMapper = postPublishedMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, PostPublishedEvent.class, event -> {
            Analytic analytic = postPublishedMapper.toAnalytic(event);
            analytic.setType(Type.POST_VIEW);
            analyticService.saveAnalytic(analytic);
        });
    }
}
