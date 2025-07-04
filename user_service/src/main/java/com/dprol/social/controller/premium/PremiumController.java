package com.dprol.social.controller.premium;

import com.dprol.social.config.UserContextConfig;
import com.dprol.social.dto.PremiumDto;
import com.dprol.social.service.premium.PremiumService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("Premium")

public class PremiumController {

    private final PremiumService premiumService;

    private final UserContextConfig userContextConfig;

    @PostMapping
    public PremiumDto activatePremium(PremiumDto premiumDto){
        Long userId = userContextConfig.getUserId();
        return premiumService.activatePremium(userId, premiumDto);
    }
}
