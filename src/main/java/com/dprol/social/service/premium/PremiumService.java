package com.dprol.social.service.premium;

import com.dprol.social.dto.PremiumDto;

public interface PremiumService {

    PremiumDto activatePremium(PremiumDto premiumDto);

    void deactivatePremium(Long id);

    void validatePremium(PremiumDto premiumDto);
}
