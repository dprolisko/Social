package com.dprol.social.scheduler;

import com.dprol.social.service.premium.PremiumService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class ClearPremium {

    private final PremiumService premiumService;

    @Scheduled(cron = "${scheduler.clearEvents.cronExpression}")
    public void removePremium() {
        premiumService.deletePremium();
    }
}
