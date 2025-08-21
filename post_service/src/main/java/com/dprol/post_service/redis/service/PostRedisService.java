package com.dprol.post_service.redis.service;

import com.dprol.post_service.redis.entity.PostRedisEntity;

public interface PostRedisService {

    void savePost(PostRedisEntity post);

    void incrementLikeCount(Long postId);

    void decrementLikeCount(Long postId);

    void incrementViewCount(Long postId);

    void deletePost(Long postId);

    void incrementCommentCount(Long postId);

    void decrementCommentCount(Long postId);
}
