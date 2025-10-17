package com.dprol.url_shortener_service.service;

import com.dprol.url_shortener_service.dto.HashDto;
import com.dprol.url_shortener_service.dto.UrlDto;
import org.springframework.web.servlet.view.RedirectView;

public interface UrlService {

    HashDto createUrl(UrlDto urlDto);

    RedirectView getRedirectView(String url);
}
