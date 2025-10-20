package com.dprol.url_shortener_service.service;

import com.dprol.url_shortener_service.dto.HashDto;
import com.dprol.url_shortener_service.dto.UrlDto;
import com.dprol.url_shortener_service.entity.Url;
import com.dprol.url_shortener_service.repository.UrlCacheRepository;
import com.dprol.url_shortener_service.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor

public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;

    private final UrlCacheRepository urlCacheRepository;

    private final HashCache hashCache;

    @Override
    public HashDto createUrl(UrlDto urlDto) {
        String hash = hashCache.getHash();
        if (hash == null) {
            throw new RuntimeException("Hash is null");
        }
        urlRepository.save(new Url(hash, urlDto.getUrl(), LocalDateTime.now()));
        urlCacheRepository.saveUrlByHash(hash, urlDto.getUrl());
        return new HashDto(hash);
    }

    @Override
    public RedirectView getRedirectView(String hash) {
        return new RedirectView(urlCacheRepository.getUrlByHash(hash).orElseGet(()-> urlRepository.getUrlByHash(hash).map(Url::getUrl).orElseThrow(()-> new RuntimeException("Hash not found"))));
    }
}
