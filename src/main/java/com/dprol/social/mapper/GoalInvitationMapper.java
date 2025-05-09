package com.dprol.social.mapper;

import com.dprol.social.dto.GoalInvitationDto;
import com.dprol.social.entity.GoalInvitation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GoalInvitationMapper {

    GoalInvitationDto toDto(GoalInvitation goalInvitation);

    @Mapping(source = "inviter", target = "inviter")
    @Mapping(source = "invited", target = "invited")
    @Mapping(source = "goal", target = "goal")
    @Mapping(source = "status", target = "status")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    GoalInvitation toEntity(GoalInvitationDto goalInvitationDto);
}
