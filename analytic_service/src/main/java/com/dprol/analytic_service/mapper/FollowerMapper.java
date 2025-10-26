package com.dprol.analytic_service.mapper;

import com.dprol.analytic_service.entity.Analytic;
import com.dprol.analytic_service.event.FollowerEvent;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FollowerMapper {

    Analytic toAnalytic(FollowerEvent followerEvent);
}
