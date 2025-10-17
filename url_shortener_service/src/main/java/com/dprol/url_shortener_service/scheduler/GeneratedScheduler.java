package com.dprol.url_shortener_service.scheduler;

import com.dprol.url_shortener_service.service.HashService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class GeneratedScheduler {

    private final HashService hashService;

    @Scheduled(cron = "${scheduler.generate.cron}")
    public void generateHash(){
        hashService.generateHashes();
    }
}
