package com.dprol.social.mapper;

import com.dprol.social.dto.UserProfileDto;
import com.dprol.social.entity.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserProfileMapper {
    UserProfileDto toDto(UserProfile userProfile);
    UserProfile toEntity(UserProfileDto userProfileDto);
}
