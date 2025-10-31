package com.dprol.analytic_service.listener;

import com.dprol.analytic_service.entity.Analytic;
import com.dprol.analytic_service.entity.Type;
import com.dprol.analytic_service.event.PremiumBoughtEvent;
import com.dprol.analytic_service.mapper.PremiumBoughtMapper;
import com.dprol.analytic_service.service.AnalyticService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component

public class PremiumBoughtListener extends Listener<PremiumBoughtEvent>{

    private final AnalyticService analyticService;

    private final PremiumBoughtMapper premiumBoughtMapper;

    public PremiumBoughtListener(ObjectMapper objectMapper, AnalyticService analyticService, PremiumBoughtMapper premiumBoughtMapper) {
        super(objectMapper);
        this.analyticService = analyticService;
        this.premiumBoughtMapper = premiumBoughtMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, PremiumBoughtEvent.class, event -> {
            Analytic analytic = premiumBoughtMapper.toAnalytic(event);
            analytic.setType(Type.POST_VIEW);
            analyticService.saveAnalytic(analytic);
        });
    }
}
