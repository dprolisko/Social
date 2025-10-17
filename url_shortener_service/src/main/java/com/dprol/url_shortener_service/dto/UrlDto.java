package com.dprol.url_shortener_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class UrlDto {

    private String url;

    private String hash;
}
