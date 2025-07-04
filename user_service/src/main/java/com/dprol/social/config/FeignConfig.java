package com.dprol.social.config;

import com.dprol.social.config.FeignUserInterceptor;
import com.dprol.social.config.UserContextConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public FeignUserInterceptor feignUserInterceptor(UserContextConfig userContextConfig) {
        return new FeignUserInterceptor(userContextConfig);
    }
}
