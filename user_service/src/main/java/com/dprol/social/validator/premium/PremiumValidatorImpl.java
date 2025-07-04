package com.dprol.social.validator.premium;

import com.dprol.social.dto.PremiumDto;
import com.dprol.social.exception.DataValidationException;
import com.dprol.social.repository.PremiumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component

public class PremiumValidatorImpl implements PremiumValidator {
    private final PremiumRepository premiumRepository;

    @Override
    public void validatePremium(PremiumDto premiumDto) {
        boolean isExists = premiumRepository.existsById(premiumDto.getPremiumId());
        if (!isExists) {
            throw new DataValidationException("There is no premium");
        }
    }
}
