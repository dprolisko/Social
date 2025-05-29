package com.dprol.social.mapper.goal;

import com.dprol.social.dto.goal.GoalDto;
import com.dprol.social.entity.goal.Goal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GoalMapper {

    @Mapping(source = "user", target = "userId")
    GoalDto toDto(Goal goal);

    Goal toEntity(GoalDto goalDto);
}
