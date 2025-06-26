package com.dprol.social.validator.event;

import com.dprol.social.dto.event.EventDto;
import com.dprol.social.entity.event.Event;
import com.dprol.social.exception.DataValidationException;
import com.dprol.social.repository.event.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class EventValidatorImpl implements EventValidator {

    private final EventRepository eventRepository;

    @Override
    public void validateEventById(Long eventId) {
        boolean isExist = eventRepository.existsById(eventId);
        if (!isExist) {
            throw new DataValidationException("Event with id " + eventId + " does not exist");
        }
    }

    @Override
    public void validateEventStatus(Event event) {
        boolean isExist = eventRepository.existsById(event.getId());
        if (!isExist) {
            throw new DataValidationException("Event with id " + event.getId() + " does not exist");
        }
    }

    @Override
    public void validateByUserId(Long userId) {
        boolean isExist = eventRepository.existsById(userId);
        if (!isExist) {
            throw new DataValidationException("Event with id " + userId + " does not exist");
        }
    }
}
