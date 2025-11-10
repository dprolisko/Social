package com.dprol.post_service.config.redis;

import com.dprol.post_service.property.RedisLockRegistryProperty;
import com.dprol.post_service.property.RedisProperty;
import com.dprol.post_service.redis.entity.AuthorRedisEntity;
import com.dprol.post_service.redis.entity.CommentRedisEntity;
import com.dprol.post_service.redis.entity.FeedRedisEntity;
import com.dprol.post_service.redis.entity.PostRedisEntity;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.convert.KeyspaceConfiguration;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.integration.support.locks.ExpirableLockRegistry;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Configuration
@RequiredArgsConstructor

public class RedisCacheConfig {

    private final RedisLockRegistryProperty redisLockRegistryProperty;

    private final RedisProperty redisProperty;

    @Bean
    public ExpirableLockRegistry expirableLockRegistry(RedisConnectionFactory redisConnectionFactory) {

        return new RedisLockRegistry(redisConnectionFactory, redisLockRegistryProperty.getPostLockKey(),
                redisLockRegistryProperty.getReleaseTimeDurationMillis());
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.of(redisProperty.getDefaultTtl(), ChronoUnit.SECONDS));
        return new RedisCacheManager(redisCacheWriter, redisCacheConfiguration);
    }

    @SuppressWarnings("unused")
    public class RedisKeyspaceConfiguration extends KeyspaceConfiguration {

        private static final List<Class<?>> entityClasses = List.of(
                PostRedisEntity.class,
                AuthorRedisEntity.class,
                CommentRedisEntity.class,
                FeedRedisEntity.class
        );

        @Override
        protected @NonNull Iterable<KeyspaceSettings> initialConfiguration() {

            return entityClasses.stream().map(this::getKeyspaceSettings).toList();
        }

        @Override
        public @NonNull KeyspaceSettings getKeyspaceSettings(@NonNull Class<?> type) {

            String cacheName = type.getAnnotation(RedisHash.class).value();

            KeyspaceSettings keyspaceSettings = new KeyspaceSettings(type, cacheName);
            keyspaceSettings.setTimeToLive(redisProperty.getCacheSettings().get(cacheName).getTtl());

            return keyspaceSettings;
        }
    }
}
