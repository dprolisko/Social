package com.dprol.post_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class HashtagDto {

    private Long id;

    @NotBlank
    private String hashtag;

    @NotBlank
    private LocalDateTime createdAt;
}
