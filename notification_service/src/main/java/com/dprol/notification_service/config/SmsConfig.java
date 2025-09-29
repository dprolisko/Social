package com.dprol.notification_service.config;

import com.vonage.client.VonageClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SmsConfig {

    @Value("${vonage.api.key}")
    private String key;

    @Value("${vonage.api.secret}")
    private String secret;

    @Bean
    public VonageClient vonageClient() {
        return VonageClient.builder().apiKey(key).apiSecret(secret).build();
    }
}

