package com.dprol.analytic_service.listener;

import com.dprol.analytic_service.entity.Analytic;
import com.dprol.analytic_service.entity.Type;
import com.dprol.analytic_service.event.ProfileViewEvent;
import com.dprol.analytic_service.mapper.ProfileViewMapper;
import com.dprol.analytic_service.service.AnalyticService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component

public class ProfileViewListener extends Listener<ProfileViewEvent> {

    private final AnalyticService analyticService;

    private final ProfileViewMapper profileViewMapper;

    public ProfileViewListener(ObjectMapper objectMapper, AnalyticService analyticService, ProfileViewMapper profileViewMapper) {
        super(objectMapper);
        this.analyticService = analyticService;
        this.profileViewMapper = profileViewMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, ProfileViewEvent.class, event -> {
            Analytic analytic = profileViewMapper.toAnalytic(event);
            analytic.setType(Type.POST_VIEW);
            analyticService.saveAnalytic(analytic);
        });
    }
}
