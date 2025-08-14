package com.dprol.post_service.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.data.redis.lock-registry")

public class RedisLockRegistryProperty {

    private String redisHost;

    private Long realiseTime;
}
