package com.dprol.post_service.redis.service;

import com.dprol.post_service.dto.UserDto;
import com.dprol.post_service.redis.entity.PostRedisEntity;
import com.dprol.post_service.redis.repository.PostRedisRepository;
import exception.DataValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor

public class PostRedisServiceImpl implements PostRedisService {

    private final PostRedisRepository postRedisRepository;

    private final RedisLockOperation redisLockOperation;

    private final AuthorRedisService authorRedisService;

    private final FeedRedisService feedRedisService;

    @Override
    public void savePost(PostRedisEntity post) {
        PostRedisEntity entity =  redisLockOperation.findById(postRedisRepository, post.getId()).orElseThrow(() -> new DataValidationException(String.format("Post with id %s not found", post.getId())));
        redisLockOperation.updateOrSave(postRedisRepository, entity, post.getId());
        CompletableFuture<UserDto> author = authorRedisService.saveAuthor(entity.getAuthorPost());
        author.thenAccept(authorDto -> authorDto.getSubscriptions().forEach(subscription ->
            feedRedisService.addPostInFeed(entity, subscription)));
    }

    @Override
    public void incrementLikeCount(Long postId) {
        redisLockOperation.findById(postRedisRepository, postId).ifPresent(post -> {post.setLikeCount(post.getLikeCount() + 1);
        redisLockOperation.updateOrSave(postRedisRepository, post, post.getId());});
    }

    @Override
    public void decrementLikeCount(Long postId) {
        redisLockOperation.findById(postRedisRepository, postId).ifPresent(post -> {post.setLikeCount(post.getLikeCount() - 1);
            redisLockOperation.updateOrSave(postRedisRepository, post, post.getId());});
    }

    @Override
    public void incrementViewCount(Long postId) {
        redisLockOperation.findById(postRedisRepository, postId).ifPresent(post -> {post.setViewCount(post.getViewCount() + 1);
            redisLockOperation.updateOrSave(postRedisRepository, post, post.getId());});
    }

    @Override
    public void deletePost(Long postId) {
        PostRedisEntity entity =  redisLockOperation.findById(postRedisRepository, postId).orElseThrow(() -> new DataValidationException(String.format("Post with id %s not found", postId)));
        if (entity != null){
            redisLockOperation.deleteById(postRedisRepository, postId);
            if (entity.getAuthorPost() != null){
                CompletableFuture<UserDto> author = authorRedisService.getAuthorByCache(entity.getAuthorPost());
                author.thenAccept(authorDto -> authorDto.getSubscriptions().forEach(
                        subscription -> feedRedisService.deletePostInFeed(entity, subscription)));
            }
        }

    }

    @Override
    public void incrementCommentCount(Long postId) {
        redisLockOperation.findById(postRedisRepository, postId).ifPresent(post -> {post.setCommentCount(post.getCommentCount() + 1);
            redisLockOperation.updateOrSave(postRedisRepository, post, post.getId());});
    }

    @Override
    public void decrementCommentCount(Long postId) {
        redisLockOperation.findById(postRedisRepository, postId).ifPresent(post -> {post.setCommentCount(post.getCommentCount() - 1);
            redisLockOperation.updateOrSave(postRedisRepository, post, post.getId());});

    }
}
