package com.dprol.analytic_service.listener;

import com.dprol.analytic_service.entity.Analytic;
import com.dprol.analytic_service.entity.Type;
import com.dprol.analytic_service.event.PostLikeEvent;
import com.dprol.analytic_service.mapper.PostLikeMapper;
import com.dprol.analytic_service.service.AnalyticService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class PostLikeListener extends Listener<PostLikeEvent>{

    private final AnalyticService analyticService;

    private final PostLikeMapper postLikeMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, PostLikeEvent.class, event -> {
            Analytic analytic = postLikeMapper.toAnalytic(event);
            analytic.setType(Type.POST_VIEW);
            analyticService.saveAnalytic(analytic);
        });
    }
}
