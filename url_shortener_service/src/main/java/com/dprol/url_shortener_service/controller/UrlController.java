package com.dprol.url_shortener_service.controller;

import com.dprol.url_shortener_service.dto.HashDto;
import com.dprol.url_shortener_service.dto.UrlDto;
import com.dprol.url_shortener_service.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
@RequestMapping("/url")

public class UrlController {

    private final UrlService urlService;

    @GetMapping("/get")
    public RedirectView getRedirectView(String hash) {
        return urlService.getRedirectView(hash);
    }

    @PostMapping("/create")
    public HashDto createUrl(UrlDto urlDto) {
        return urlService.createUrl(urlDto);
    }
}
