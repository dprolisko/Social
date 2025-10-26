package com.dprol.analytic_service.listener;

import com.dprol.analytic_service.entity.Analytic;
import com.dprol.analytic_service.entity.Type;
import com.dprol.analytic_service.event.PostCommentEvent;
import com.dprol.analytic_service.mapper.PostCommentMapper;
import com.dprol.analytic_service.service.AnalyticService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class PostCommentListener extends Listener<PostCommentEvent> {

    private final AnalyticService analyticService;

    private final PostCommentMapper postCommentMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, PostCommentEvent.class, event -> {
            Analytic analytic = postCommentMapper.toAnalytic(event);
            analytic.setType(Type.POST_VIEW);
            analyticService.saveAnalytic(analytic);
        });
    }
}
