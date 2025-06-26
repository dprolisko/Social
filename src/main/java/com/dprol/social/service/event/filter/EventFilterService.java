package com.dprol.social.service.event.filter;

import com.dprol.social.dto.event.EventFilterDto;
import com.dprol.social.entity.event.Event;

import java.util.stream.Stream;

public interface EventFilterService {

    Stream<Event> filterEvents(Stream<Event> events, EventFilterDto eventFilterDto);
}
