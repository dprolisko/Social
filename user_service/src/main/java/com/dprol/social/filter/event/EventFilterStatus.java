package com.dprol.social.filter.event;

import com.dprol.social.dto.event.EventFilterDto;
import com.dprol.social.entity.event.Event;
import com.dprol.social.filter.event.EventFilter;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component

public class EventFilterStatus implements EventFilter {

    @Override
    public boolean booleanFilter(EventFilterDto eventFilterDto) {
        return eventFilterDto.getStatus() != null;
    }

    @Override
    public Stream<Event> filterEvents(Stream<Event> events, EventFilterDto eventFilterDto) {
        return events.filter(e -> e.getEventStatus().equals(eventFilterDto.getStatus()));
    }
}
