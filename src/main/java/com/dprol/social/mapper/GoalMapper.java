package com.dprol.social.mapper;

import com.dprol.social.dto.GoalDto;
import com.dprol.social.entity.Goal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GoalMapper {

    @Mapping(source = "user", target = "userId")
    GoalDto toDto(Goal goal);

    Goal toEntity(GoalDto goalDto);
}
