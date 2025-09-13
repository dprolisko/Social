package com.dprol.post_service.redis.service;

import com.dprol.post_service.redis.entity.FeedRedisEntity;
import com.dprol.post_service.redis.entity.PostRedisEntity;

public interface FeedRedisService {

    void addPostInFeed(PostRedisEntity post, Long authorId);

    void deletePostInFeed(PostRedisEntity post, Long authorId);

    FeedRedisEntity getPostInFeed(Long authorId);
}
