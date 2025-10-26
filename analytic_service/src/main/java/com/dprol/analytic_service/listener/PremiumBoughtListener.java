package com.dprol.analytic_service.listener;

import com.dprol.analytic_service.entity.Analytic;
import com.dprol.analytic_service.entity.Type;
import com.dprol.analytic_service.event.PremiumBoughtEvent;
import com.dprol.analytic_service.mapper.PremiumBoughtMapper;
import com.dprol.analytic_service.service.AnalyticService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class PremiumBoughtListener extends Listener<PremiumBoughtEvent>{

    private final AnalyticService analyticService;

    private final PremiumBoughtMapper premiumBoughtMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, PremiumBoughtEvent.class, event -> {
            Analytic analytic = premiumBoughtMapper.toAnalytic(event);
            analytic.setType(Type.POST_VIEW);
            analyticService.saveAnalytic(analytic);
        });
    }
}
