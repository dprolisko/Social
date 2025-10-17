package com.dprol.url_shortener_service.client;

import com.dprol.url_shortener_service.client.context.UserContextConfig;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FeignUserInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("UserId",String.valueOf(userContextConfig.getUserId()));
    }

    private final UserContextConfig userContextConfig;
}
