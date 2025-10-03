package com.dprol.notification_service.client;

import com.dprol.notification_service.client.context.UserContextConfig;
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
