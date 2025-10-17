package com.dprol.url_shortener_service.client;


import com.dprol.url_shortener_service.client.context.UserContextConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public FeignUserInterceptor feignUserInterceptor(UserContextConfig userContextConfig) {
        return new FeignUserInterceptor(userContextConfig);
    }
}
