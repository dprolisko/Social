package com.dprol.social.validator.event;

import com.dprol.social.dto.event.EventDto;

public interface EventValidator {

    void validate(EventDto eventDto);

    void validateByUserId(Long userId);
}