package com.dprol.social.dto.event;

import com.dprol.social.entity.event.EventStatus;
import com.dprol.social.entity.event.EventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class EventDto {

    private Long id;

    @NotBlank
    @Size(max = 16)
    private String name;

    @Size(max=256)
    private String description;

    private LocalDateTime start;

    private LocalDateTime end;

    @NotNull
    private Long ownerId;

    private EventType eventType;

    private EventStatus eventStatus;
}
