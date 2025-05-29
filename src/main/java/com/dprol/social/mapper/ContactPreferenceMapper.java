package com.dprol.social.mapper;

import com.dprol.social.dto.ContactPreferenceDto;
import com.dprol.social.entity.contact.ContactPreference;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ContactPreferenceMapper {

    ContactPreferenceDto toDto(ContactPreference contactPreference);

    ContactPreference toEntity(ContactPreferenceDto contactPreferenceDto);
}
