package com.dprol.post_service.config;

import org.springframework.stereotype.Component;

@Component
public class UserContextConfig {

    private final ThreadLocal<Long> userId = new ThreadLocal<>();

    public Long getUserId() {
        return userId.get();
    }

    public void setUserId(Long userId) {
        this.userId.set(userId);
    }

    public void clearUserId() {
        userId.remove();
    }
}
