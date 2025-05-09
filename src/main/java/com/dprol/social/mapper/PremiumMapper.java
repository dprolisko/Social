package com.dprol.social.mapper;

import com.dprol.social.entity.Premium;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PremiumMapper {

    @Mapping(source = "id", target = "user.id")
    Premium toEntity(Long id, LocalDateTime startDataTime, LocalDateTime endDataTime);
}
