package com.dprol.social.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class RatingDto {

    private Long id;

    @NotBlank
    private  int rating;
}
