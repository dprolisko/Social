package com.dprol.analytic_service.client;


import com.dprol.analytic_service.client.context.UserContextConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public FeignUserInterceptor feignUserInterceptor(UserContextConfig userContextConfig) {
        return new FeignUserInterceptor(userContextConfig);
    }
}
