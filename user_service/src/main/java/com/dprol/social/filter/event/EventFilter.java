package com.dprol.social.filter.event;

import com.dprol.social.dto.event.EventFilterDto;
import com.dprol.social.entity.event.Event;

import java.util.stream.Stream;

public interface EventFilter {

    boolean booleanFilter(EventFilterDto eventFilterDto);

    Stream<Event> filterEvents(Stream<Event> events,EventFilterDto eventFilterDto);
}
