package com.dprol.post_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ExecutorConfig {

    @Bean
    public Executor executor(@Value("${spelling.threads}") int count) {
        return Executors.newFixedThreadPool(count);
    }

    @Bean
    public ExecutorService executorService(@Value("${moderation.threads-count}") int count) {
        return Executors.newFixedThreadPool(count);
    }
}
