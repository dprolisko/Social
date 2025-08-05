package com.dprol.social.mapper.goal;

import com.dprol.social.dto.goal.GoalInvitationDto;
import com.dprol.social.entity.goal.Goal;
import com.dprol.social.entity.goal.GoalInvitation;
import com.dprol.social.entity.goal.GoalStatus;
import com.dprol.social.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GoalInvitationMapper {

    GoalInvitationDto toDto(GoalInvitation goalInvitation);

    @Mapping(source = "goal", target = "goal")
    @Mapping(source = "status", target = "status")
    @Mapping(target = "id", ignore = true)
    GoalInvitation toEntity(User inviter, User invited, Goal goal, GoalStatus status);
}
