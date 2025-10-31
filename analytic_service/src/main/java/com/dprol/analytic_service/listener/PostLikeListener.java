package com.dprol.analytic_service.listener;

import com.dprol.analytic_service.entity.Analytic;
import com.dprol.analytic_service.entity.Type;
import com.dprol.analytic_service.event.PostLikeEvent;
import com.dprol.analytic_service.mapper.PostLikeMapper;
import com.dprol.analytic_service.service.AnalyticService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component

public class PostLikeListener extends Listener<PostLikeEvent>{

    private final AnalyticService analyticService;

    private final PostLikeMapper postLikeMapper;

    public PostLikeListener(ObjectMapper objectMapper, AnalyticService analyticService, PostLikeMapper postLikeMapper) {
        super(objectMapper);
        this.analyticService = analyticService;
        this.postLikeMapper = postLikeMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, PostLikeEvent.class, event -> {
            Analytic analytic = postLikeMapper.toAnalytic(event);
            analytic.setType(Type.POST_VIEW);
            analyticService.saveAnalytic(analytic);
        });
    }
}
