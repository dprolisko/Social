package com.dprol.analytic_service.listener;

import com.dprol.analytic_service.entity.Analytic;
import com.dprol.analytic_service.entity.Type;
import com.dprol.analytic_service.event.ProfileViewEvent;
import com.dprol.analytic_service.mapper.ProfileViewMapper;
import com.dprol.analytic_service.service.AnalyticService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class ProfileViewListener extends Listener<ProfileViewEvent> {

    private final AnalyticService analyticService;

    private final ProfileViewMapper profileViewMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, ProfileViewEvent.class, event -> {
            Analytic analytic = profileViewMapper.toAnalytic(event);
            analytic.setType(Type.POST_VIEW);
            analyticService.saveAnalytic(analytic);
        });
    }
}
