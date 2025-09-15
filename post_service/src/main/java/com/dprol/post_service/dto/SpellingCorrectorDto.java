package com.dprol.post_service.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized

public class SpellingCorrectorDto {

    private String word;

    private String[] variantWords;
}
