package com.dprol.social.mapper;

import com.dprol.social.dto.PremiumDto;
import com.dprol.social.entity.Premium;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PremiumMapper {

    Premium toEntity(Long userId, PremiumDto premiumDto);

    PremiumDto toDto(Premium premium);
}
