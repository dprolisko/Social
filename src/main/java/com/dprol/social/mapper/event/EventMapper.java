package com.dprol.social.mapper.event;

import com.dprol.social.dto.event.EventDto;
import com.dprol.social.entity.event.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    @Mapping(source = "user", target = "id")
    EventDto toDto(Event event);

    Event toEntity(EventDto eventDto);
}
