package com.dprol.social.service.premium;

import com.dprol.social.dto.PremiumDto;
import com.dprol.social.entity.Premium;
import com.dprol.social.event.PremiumBuyEvent;
import com.dprol.social.exception.PremiumNotFoundException;
import com.dprol.social.mapper.PremiumMapper;
import com.dprol.social.publisher.PremiumBuyPublisher;
import com.dprol.social.repository.PremiumRepository;
import com.dprol.social.validator.premium.PremiumValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class PremiumServiceImpl implements PremiumService {

    private final PremiumRepository premiumRepository;

    private final PremiumMapper premiumMapper;

    private final PremiumValidator premiumValidator;

    private final PremiumBuyPublisher premiumBuyPublisher;

    @Override
    public PremiumDto activatePremium(PremiumDto premiumDto) {
        premiumDto.setUserId(premiumDto.getUserId());
        premiumDto.setPremiumId(premiumDto.getPremiumId());
        Premium premium = premiumMapper.toEntity(premiumDto);
        if (premium.getId().equals(premiumDto.getPremiumId())) {
            premiumBuyPublisher.publisher(new PremiumBuyEvent(premiumDto.getPremiumId(), premiumDto.getUserId(), premiumDto.getPeriod(), premiumDto.getStartTime()));
        }
        premiumRepository.save(premium);
        return premiumDto;
    }

    @Override
    public void deactivatePremium(Long id) {
        Premium premium = findPremiumById(id);
        premiumRepository.delete(premium);
    }

    @Override
    public void validatePremium(PremiumDto premiumDto) {
        premiumValidator.validatePremium(premiumDto);
    }

    public Premium findPremiumById(Long id) {
        return premiumRepository.findById(id).orElseThrow(() -> new PremiumNotFoundException(String.format("Premium Id:" + id + "not found")));
    }
}
