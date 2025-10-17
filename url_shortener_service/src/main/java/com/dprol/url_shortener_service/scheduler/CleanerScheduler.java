package com.dprol.url_shortener_service.scheduler;

import com.dprol.url_shortener_service.entity.Url;
import com.dprol.url_shortener_service.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class CleanerScheduler {

    private final UrlRepository urlRepository;

    @Scheduled(cron = "${scheduler.clear.cron}")
    public void deleteOldUrls() {
        List<Url> urls = urlRepository.findOldUrls();
        if (urls != null) {
            urlRepository.deleteAll(urls);
        }
    }
}
