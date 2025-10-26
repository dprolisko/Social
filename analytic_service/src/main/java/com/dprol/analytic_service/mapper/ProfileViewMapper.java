package com.dprol.analytic_service.mapper;

import com.dprol.analytic_service.entity.Analytic;
import com.dprol.analytic_service.event.ProfileViewEvent;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProfileViewMapper {

    Analytic toAnalytic(ProfileViewEvent profileViewEvent);
}
