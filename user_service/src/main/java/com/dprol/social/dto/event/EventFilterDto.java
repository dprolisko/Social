package com.dprol.social.dto.event;

import com.dprol.social.entity.event.EventStatus;
import com.dprol.social.entity.event.EventType;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class EventFilterDto {

    private EventStatus status;

    private EventType eventType;

    private LocalDateTime start;

    private LocalDateTime end;
}
