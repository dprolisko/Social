package com.dprol.social.filter.event;

import com.dprol.social.dto.event.EventFilterDto;
import com.dprol.social.entity.event.Event;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component

public class EventFilterEnd implements EventFilter {

    @Override
    public boolean booleanFilter(EventFilterDto eventFilterDto) {
        return eventFilterDto.getEnd() != null;
    }

    @Override
    public Stream<Event> filterEvents(Stream<Event> events, EventFilterDto eventFilterDto) {
        return events.filter(e -> e.getEnd().equals(eventFilterDto.getEnd()));
    }
}
