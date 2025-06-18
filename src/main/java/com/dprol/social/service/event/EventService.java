package com.dprol.social.service.event;

import com.dprol.social.dto.event.EventDto;

import java.util.List;

public interface EventService {

    EventDto createEvent(EventDto eventDto);

    void deleteEvent(Long id);

    EventDto updateEvent(EventDto eventDto);

    List<EventDto> getListEvents(Long id);

    List<EventDto> getParticipatedEvents(Long id);

    List<EventDto> getOwnedEvents(Long id);

    void clearEvents();
}