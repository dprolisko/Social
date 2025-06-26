package com.dprol.social.service.event;

import com.dprol.social.dto.event.EventDto;
import com.dprol.social.dto.event.EventFilterDto;
import com.dprol.social.entity.event.Event;
import com.dprol.social.entity.user.User;
import com.dprol.social.exception.EventNotFoundException;
import com.dprol.social.exception.GoalNotFoundException;
import com.dprol.social.exception.UserNotFoundException;
import com.dprol.social.filter.event.EventFilter;
import com.dprol.social.mapper.event.EventMapper;
import com.dprol.social.repository.UserRepository;
import com.dprol.social.repository.event.EventRepository;
import com.dprol.social.service.event.filter.EventFilterService;
import com.dprol.social.validator.event.EventValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor

public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    private final EventMapper eventMapper;

    private final EventValidator eventValidator;

    private final EventFilterService eventFilterService;

    private final UserRepository userRepository;

    @Override
    public EventDto createEvent(EventDto eventDto) {
        Event event = eventMapper.toEntity(eventDto);
        eventRepository.save(event);
        return eventMapper.toDto(event);
    }

    @Override
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    @Override
    public EventDto updateEvent(EventDto eventDto) {
        Event eventValid = findEventById(eventDto.getId());
        eventValidator.validateEventStatus(eventValid);
        Event event = eventMapper.toEntity(eventDto);
        event = eventRepository.save(event);
        return eventMapper.toDto(event);
    }

    @Override
    public List<EventDto> getListEvents(Long eventId, EventFilterDto eventFilterDto) {
        eventValidator.validateEventById(eventId);
        Stream<Event> filterEvent = eventRepository.findAllByEventId(eventId);
        return eventFilterService.filterEvents(filterEvent, eventFilterDto).map(eventMapper::toDto).toList();
    }

    @Override
    public List<EventDto> getParticipatedEvents(Long id, EventFilterDto eventFilterDto) {
        return List.of();
    }

    @Override
    public List<EventDto> getOwnedEvents(Long id, EventFilterDto eventFilterDto) {
        return List.of();
    }

    @Override
    public Event findEventById(Long eventId) {
        return eventRepository.findEventById(eventId).orElseThrow(() -> new EventNotFoundException(String.format("Event with id %s not found", eventId)));
    }

    @Override
    public void clearEvents() {

    }
}
