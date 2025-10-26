package com.dprol.analytic_service.listener;

import com.dprol.analytic_service.entity.Analytic;
import com.dprol.analytic_service.entity.Type;
import com.dprol.analytic_service.event.AchievementReceivedEvent;
import com.dprol.analytic_service.event.PostCommentEvent;
import com.dprol.analytic_service.mapper.AchievementReceivedMapper;
import com.dprol.analytic_service.mapper.PostCommentMapper;
import com.dprol.analytic_service.service.AnalyticService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class AchievementReceivedListener extends Listener<AchievementReceivedEvent>{

    private final AnalyticService analyticService;

    private final AchievementReceivedMapper achievementReceivedMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, AchievementReceivedEvent.class, event -> {
            Analytic analytic = achievementReceivedMapper.toAnalytic(event);
            analytic.setType(Type.POST_VIEW);
            analyticService.saveAnalytic(analytic);
        });
    }
}
