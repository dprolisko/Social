package com.dprol.social.filter.event;

import com.dprol.social.dto.event.EventFilterDto;
import com.dprol.social.entity.event.Event;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component

public class EventFilterType implements EventFilter {

    @Override
    public boolean booleanFilter(EventFilterDto eventFilterDto) {
        return eventFilterDto.getEventType() != null;
    }

    @Override
    public Stream<Event> filterEvents(Stream<Event> events, EventFilterDto eventFilterDto) {
        return events.filter(e -> e.getEventType().equals(eventFilterDto.getEventType()));
    }
}
