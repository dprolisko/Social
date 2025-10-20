package com.dprol.url_shortener_service.service;

import com.dprol.url_shortener_service.dto.HashDto;
import com.dprol.url_shortener_service.dto.UrlDto;
import com.dprol.url_shortener_service.entity.Url;
import com.dprol.url_shortener_service.repository.UrlCacheRepository;
import com.dprol.url_shortener_service.repository.UrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlServiceImplTest {

    @Mock
    private UrlRepository urlRepository;
    @Mock
    private UrlCacheRepository urlCacheRepository;
    @Mock
    private HashCache hashCache;

    @InjectMocks
    private UrlServiceImpl urlService;

    private UrlDto urlDto;

    @BeforeEach
    void setUp() {
        urlDto = UrlDto.builder().url("https://example.com").build();
    }

    @Test
    void createUrl_ShouldReturnHashDto_WhenHashExists() {
        when(hashCache.getHash()).thenReturn("abc123");

        HashDto result = urlService.createUrl(urlDto);

        assertNotNull(result);
        assertEquals("abc123", result.getHash());
        verify(urlRepository).save(any(Url.class));
        verify(urlCacheRepository).saveUrlByHash("abc123", "https://example.com");
    }

    @Test
    void createUrl_ShouldThrow_WhenHashIsNull() {
        when(hashCache.getHash()).thenReturn(null);

        assertThrows(RuntimeException.class, () -> urlService.createUrl(urlDto));

        verify(urlRepository, never()).save(any());
        verify(urlCacheRepository, never()).saveUrlByHash(any(), any());
    }

    @Test
    void getRedirectView_ShouldReturnFromCache_WhenExists() {
        when(urlCacheRepository.getUrlByHash("abc123")).thenReturn(Optional.of("https://cached.com"));

        RedirectView result = urlService.getRedirectView("abc123");

        assertEquals("https://cached.com", result.getUrl());
        verify(urlCacheRepository).getUrlByHash("abc123");
        verify(urlRepository, never()).getUrlByHash(any());
    }

    @Test
    void getRedirectView_ShouldReturnFromRepository_WhenNotInCache() {
        String hash = "123";
        String url = "https://db.com";
        Url urlEntity = new Url();
        urlEntity.setUrl(url);
        when(urlCacheRepository.getUrlByHash(anyString())).thenReturn(Optional.empty());
        when(urlRepository.getUrlByHash(anyString())).thenReturn(Optional.of(urlEntity));

        RedirectView result = urlService.getRedirectView(hash);

        assertEquals(url, result.getUrl());

        verify(urlCacheRepository).getUrlByHash(hash);
        verify(urlRepository).getUrlByHash(hash);
    }

    @Test
    void getRedirectView_ShouldThrow_WhenNotFoundAnywhere() {
        when(urlCacheRepository.getUrlByHash("abc123")).thenReturn(Optional.empty());
        when(urlRepository.getUrlByHash("abc123")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> urlService.getRedirectView("abc123"));

        verify(urlCacheRepository).getUrlByHash("abc123");
        verify(urlRepository).getUrlByHash("abc123");
    }
}
