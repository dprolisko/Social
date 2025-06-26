package com.dprol.social.service.event;

import com.dprol.social.dto.event.EventDto;
import com.dprol.social.dto.event.EventFilterDto;
import com.dprol.social.entity.event.Event;

import java.util.List;

public interface EventService {

    EventDto createEvent(EventDto eventDto);

    void deleteEvent(Long id);

    EventDto updateEvent(EventDto eventDto);

    List<EventDto> getListEvents(Long id, EventFilterDto eventFilterDto);

    List<EventDto> getParticipatedEvents(Long eventId, EventFilterDto eventFilterDto);

    List<EventDto> getOwnedEvents(Long eventId, EventFilterDto eventFilterDto);

    Event findEventById(Long eventId);

    void clearEvents();
}