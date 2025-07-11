package com.dprol.social.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CountryDto {

    private Long id;

    @NotBlank
    private String countryName;
}
