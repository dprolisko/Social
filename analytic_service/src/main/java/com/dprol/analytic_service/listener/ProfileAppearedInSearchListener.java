package com.dprol.analytic_service.listener;

import com.dprol.analytic_service.entity.Analytic;
import com.dprol.analytic_service.entity.Type;
import com.dprol.analytic_service.event.ProfileAppearedInSearchEvent;
import com.dprol.analytic_service.mapper.ProfileAppearedInSearchMapper;
import com.dprol.analytic_service.service.AnalyticService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class ProfileAppearedInSearchListener extends Listener<ProfileAppearedInSearchEvent>{

    private final AnalyticService analyticService;

    private final ProfileAppearedInSearchMapper profileAppearedInSearchMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, ProfileAppearedInSearchEvent.class, event -> {
            Analytic analytic = profileAppearedInSearchMapper.toAnalytic(event);
            analytic.setType(Type.POST_VIEW);
            analyticService.saveAnalytic(analytic);
        });
    }
}
