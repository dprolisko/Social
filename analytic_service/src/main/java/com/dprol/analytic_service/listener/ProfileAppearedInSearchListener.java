package com.dprol.analytic_service.listener;

import com.dprol.analytic_service.entity.Analytic;
import com.dprol.analytic_service.entity.Type;
import com.dprol.analytic_service.event.ProfileAppearedInSearchEvent;
import com.dprol.analytic_service.mapper.ProfileAppearedInSearchMapper;
import com.dprol.analytic_service.service.AnalyticService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component

public class ProfileAppearedInSearchListener extends Listener<ProfileAppearedInSearchEvent>{

    private final AnalyticService analyticService;

    private final ProfileAppearedInSearchMapper profileAppearedInSearchMapper;

    public ProfileAppearedInSearchListener(ObjectMapper objectMapper, AnalyticService analyticService, ProfileAppearedInSearchMapper profileAppearedInSearchMapper) {
        super(objectMapper);
        this.analyticService = analyticService;
        this.profileAppearedInSearchMapper = profileAppearedInSearchMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, ProfileAppearedInSearchEvent.class, event -> {
            Analytic analytic = profileAppearedInSearchMapper.toAnalytic(event);
            analytic.setType(Type.POST_VIEW);
            analyticService.saveAnalytic(analytic);
        });
    }
}
