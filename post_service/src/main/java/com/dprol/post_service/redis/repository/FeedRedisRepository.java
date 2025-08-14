package com.dprol.post_service.redis.repository;

import com.dprol.post_service.redis.entity.FeedRedisEntity;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface FeedRedisRepository extends KeyValueRepository<FeedRedisEntity, Long> {
}
