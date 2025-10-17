package com.dprol.url_shortener_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor

public class HashDto {

    private String hash;
}
