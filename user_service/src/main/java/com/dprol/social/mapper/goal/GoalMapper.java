package com.dprol.social.mapper.goal;

import com.dprol.social.dto.goal.GoalDto;
import com.dprol.social.entity.goal.Goal;
import com.dprol.social.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GoalMapper {

    @Mapping(source = "users", target = "usersIds", qualifiedByName = "mapUsersToId")
    GoalDto toDto(Goal goal);

    Goal toEntity(GoalDto goalDto);

    @Named("mapUsersToId")
    default List<Long> mapUsersToId(List<User> users){
        return users.stream().map(User::getId).collect(Collectors.toList());
    }
}
