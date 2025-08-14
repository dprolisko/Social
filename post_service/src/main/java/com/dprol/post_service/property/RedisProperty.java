package com.dprol.post_service.property;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@ConfigurationProperties(prefix = "spring.data.redis.cache")

public class RedisProperty {

    private Long defaultTtl;
    private Map<String, CacheSettings> cacheSettings;

    @Data
    public static class CacheSettings {

        private String name;
        private Long ttl;
    }

    @PostConstruct
    public void init() {
        cacheSettings.forEach((key, value) -> {
            if (value.getTtl() == null) {
                value.setTtl(defaultTtl);
            }
        });
    }
}
