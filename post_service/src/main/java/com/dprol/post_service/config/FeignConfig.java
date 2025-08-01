package com.dprol.post_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public FeignUserInterceptor feignUserInterceptor(UserContextConfig userContextConfig) {
        return new FeignUserInterceptor(userContextConfig);
    }
}
