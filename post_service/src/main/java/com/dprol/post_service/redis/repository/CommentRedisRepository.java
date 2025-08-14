package com.dprol.post_service.redis.repository;

import com.dprol.post_service.redis.entity.CommentRedisEntity;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface CommentRedisRepository extends KeyValueRepository<CommentRedisEntity, Long> {
}
