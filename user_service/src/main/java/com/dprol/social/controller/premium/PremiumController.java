package com.dprol.social.controller.premium;

import com.dprol.social.config.UserContextConfig;
import com.dprol.social.dto.PremiumDto;
import com.dprol.social.service.premium.PremiumService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
