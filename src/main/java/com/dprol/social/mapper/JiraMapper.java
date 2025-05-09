package com.dprol.social.mapper;

import com.dprol.social.dto.JiraDto;
import com.dprol.social.entity.Jira;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JiraMapper {

    @Mapping(source = "user.id", target = "userId")
    JiraDto toDto(Jira jira);

    @Mapping(source = "userId", target = "user.id")
    Jira toEntity(JiraDto jiraDto);
}
