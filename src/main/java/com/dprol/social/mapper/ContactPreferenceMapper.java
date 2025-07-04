package com.dprol.social.mapper;

import com.dprol.social.dto.ContactPreferenceDto;
import com.dprol.social.entity.contact.ContactPreference;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ContactPreferenceMapper {

    @Mapping(source = "user.id", target = "userId")
    ContactPreferenceDto toDto(ContactPreference contactPreference);

    @Mapping(source = "userId", target = "user.id")
    ContactPreference toEntity(ContactPreferenceDto contactPreferenceDto);
}
