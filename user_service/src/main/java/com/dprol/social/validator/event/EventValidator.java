package com.dprol.social.validator.event;

import com.dprol.social.entity.event.Event;

public interface EventValidator {

    void validateEventById(Long eventId);

    void validateEventStatus(Event event);

    void validateByUserId(Long userId);
}