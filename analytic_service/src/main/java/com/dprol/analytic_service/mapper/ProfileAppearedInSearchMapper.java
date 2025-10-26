package com.dprol.analytic_service.mapper;

import com.dprol.analytic_service.entity.Analytic;
import com.dprol.analytic_service.event.ProfileAppearedInSearchEvent;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProfileAppearedInSearchMapper {

    Analytic toAnalytic(ProfileAppearedInSearchEvent profileAppearedInSearchEvent);
}
