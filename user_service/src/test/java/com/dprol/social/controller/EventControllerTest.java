package com.dprol.social.controller;

import com.dprol.social.controller.event.EventController;
import com.dprol.social.dto.SkillDto;
import com.dprol.social.dto.event.EventDto;
import com.dprol.social.dto.event.EventFilterDto;
import com.dprol.social.entity.event.Event;
import com.dprol.social.entity.event.EventStatus;
import com.dprol.social.entity.event.EventType;
import com.dprol.social.service.event.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    @Mock
    private EventService eventService;

    private EventDto eventDto;
    private Event event;
    private EventFilterDto filterDto;
    private ObjectMapper objectMapper;
    @InjectMocks
    private EventController eventController;

    @BeforeEach
    void init(){
        event = Event.builder().id(1L).name("name").description("description").start(LocalDateTime.now()).end(LocalDateTime.now().plusDays(1)).build();
        eventDto = EventDto.builder()
                .ownerId(1L)
                .name("dto")
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1L))
                .build();
        filterDto = EventFilterDto.builder()
                .status(EventStatus.active)
                .eventType(EventType.webinar)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .build();
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();
        objectMapper = new ObjectMapper();
    }
    @Test
    void createEvent_ShouldReturnCreated() throws Exception {
        when(eventService.createEvent(eventDto)).thenReturn(eventDto);
        assertEquals(eventDto, eventController.createEvent(eventDto));
    }

    @Test
    void updateEvent_ShouldReturnOk() throws Exception {
        when(eventService.updateEvent(any(EventDto.class))).thenReturn(eventDto);
        assertEquals(eventDto, eventController.updateEvent(eventDto));
    }

    @Test
    void getListEvents_ShouldReturnList() throws Exception {
        List<EventDto> events = Collections.singletonList(eventDto);
        when(eventService.getListEvents(anyLong(), any(EventFilterDto.class))).thenReturn(events);
        assertIterableEquals(events, eventController.getListEvents(1L, filterDto));
    }

    @Test
    void getParticipatedEvents_ShouldReturnList() throws Exception {
        List<EventDto> events = Collections.singletonList(eventDto);
        when(eventService.getParticipatedEvents(anyLong(), any(EventFilterDto.class))).thenReturn(events);
        assertIterableEquals(events, eventController.getParticipatedEvents(1L, filterDto));
    }

    @Test
    void getOwnedEvents_ShouldReturnList() throws Exception {
        List<EventDto> events = Collections.singletonList(eventDto);
        when(eventService.getOwnedEvents(anyLong(), any(EventFilterDto.class))).thenReturn(events);
        assertIterableEquals(events, eventController.getOwnedEvents(1L, filterDto));
    }
}
