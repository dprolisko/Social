package com.dprol.social.service.event.filter;

import com.dprol.social.dto.event.EventFilterDto;
import com.dprol.social.entity.event.Event;
import com.dprol.social.filter.event.EventFilter;
import com.dprol.social.service.event.filter.EventFilterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor

public class EventFilterServiceImpl implements EventFilterService {

    private final List<EventFilter> eventFilters;

    @Override
    public Stream<Event> filterEvents(Stream<Event> events, EventFilterDto eventFilterDto) {
        if (eventFilterDto != null) {
            for (EventFilter eventFilter : eventFilters) {
                if (eventFilter.booleanFilter(eventFilterDto)) {
                    events = eventFilter.filterEvents(events, eventFilterDto);
                }
            }
        }
        return events;
    }
}
