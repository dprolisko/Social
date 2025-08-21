package com.dprol.post_service.redis.service;

import com.dprol.post_service.redis.entity.FeedRedisEntity;
import com.dprol.post_service.redis.entity.PostRedisEntity;
import com.dprol.post_service.redis.repository.FeedRedisRepository;
import exception.DataValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.NavigableSet;
import java.util.TreeSet;

@Service
@RequiredArgsConstructor

public class FeedRedisServiceImpl implements FeedRedisService {

    private final FeedRedisRepository feedRedisRepository;

    private final RedisLockOperation redisLockOperation;

    @Value("${spring.data.redis.cache.settings.max-feed-size}")
    private Long maxFeedSize;

    @Override
    public void addPostInFeed(PostRedisEntity post, Long authorId) {
        FeedRedisEntity feedRedisEntity = redisLockOperation.findById(feedRedisRepository, authorId).orElseThrow(() -> new DataValidationException(String.format("Not found post with id %d", authorId)));
        if (feedRedisEntity == null) {
            NavigableSet<PostRedisEntity> postRedisEntity = new TreeSet<>();
            postRedisEntity.add(post);
            feedRedisEntity = feedRedisEntity.builder().id(authorId).feedPosts(postRedisEntity).build();
        }
        else {
            NavigableSet<PostRedisEntity> postRedisEntity = feedRedisEntity.getFeedPosts();
            postRedisEntity.add(post);
            while (postRedisEntity.size() > maxFeedSize) {
                postRedisEntity.pollLast();
            }
        }
        redisLockOperation.updateOrSave(feedRedisRepository, feedRedisEntity, authorId);
    }

    @Override
    public void deletePostInFeed(PostRedisEntity post, Long authorId) {
        FeedRedisEntity feedRedisEntity = redisLockOperation.findById(feedRedisRepository, authorId).orElseThrow(() -> new DataValidationException(String.format("Not found post with id %d", authorId)));
        if (feedRedisEntity != null) {
            NavigableSet<PostRedisEntity> postRedisEntity = feedRedisEntity.getFeedPosts();
            postRedisEntity.remove(post);
            redisLockOperation.updateOrSave(feedRedisRepository, feedRedisEntity, authorId);
        }
    }
}
