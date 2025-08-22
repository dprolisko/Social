package com.dprol.post_service.redis.service;

import com.dprol.post_service.redis.entity.CommentRedisEntity;

public interface CommentRedisService {

    void saveComment(CommentRedisEntity comment);

    void deleteComment(Long commentId);

    void incrementComment(Long commentId);

    void decrementComment(Long commentId);
}
