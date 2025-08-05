package com.dprol.social.controller.event;

import com.dprol.social.dto.event.EventDto;
import com.dprol.social.dto.event.EventFilterDto;
import com.dprol.social.entity.event.Event;
import com.dprol.social.service.event.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/event")

public class EventController {

    private final EventService eventService;

    @PostMapping("/create")
    public EventDto createEvent(EventDto eventDto){
        return eventService.createEvent(eventDto);
    }

    @PutMapping("/update")
    public EventDto updateEvent(EventDto eventDto){
        return eventService.updateEvent(eventDto);
    }

    @GetMapping("/list")
    public List<EventDto> getListEvents(Long id, EventFilterDto eventFilterDto){
        return eventService.getListEvents(id, eventFilterDto);
    }

    @GetMapping("/getParticipated")
    public List<EventDto> getParticipatedEvents(Long userId, EventFilterDto eventFilterDto){
        return eventService.getParticipatedEvents(userId, eventFilterDto);
    }

    @GetMapping("/get")
    public List<EventDto> getOwnedEvents(Long userId, EventFilterDto eventFilterDto){
        return eventService.getOwnedEvents(userId, eventFilterDto);
    }

    @GetMapping("/find")
    public Event findEventById(Long eventId){
        return eventService.findEventById(eventId);
    }
}