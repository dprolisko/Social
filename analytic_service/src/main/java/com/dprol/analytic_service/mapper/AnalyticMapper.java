package com.dprol.analytic_service.mapper;

import com.dprol.analytic_service.dto.AnalyticDto;
import com.dprol.analytic_service.entity.Analytic;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticMapper {

    Analytic toEntity(AnalyticDto analyticDto);
    AnalyticDto toDto(Analytic analytic);
}
