package com.dprol.post_service.redis.service;

import com.dprol.post_service.dto.UserDto;
import com.dprol.post_service.redis.entity.CommentRedisEntity;
import com.dprol.post_service.redis.repository.CommentRedisRepository;
import com.dprol.post_service.exception.DataValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor

public class CommentRedisServiceImpl implements CommentRedisService {

    private final CommentRedisRepository commentRedisRepository;

    private final RedisLockOperation redisLockOperation;

    private final AuthorRedisService authorRedisService;

    @Override
    public void saveComment(CommentRedisEntity comment) {
        CommentRedisEntity commentRedisEntity = redisLockOperation.findById(commentRedisRepository, comment.getId()).orElseThrow(()-> new DataValidationException(String.format("Comment with id %s not found", comment.getId())));
        redisLockOperation.updateOrSave(commentRedisRepository, commentRedisEntity, commentRedisEntity.getId());
        CompletableFuture<UserDto> author = authorRedisService.saveAuthor(commentRedisEntity.getAuthorComment());
    }

    @Override
    public void deleteComment(Long commentId) {
        CommentRedisEntity commentRedisEntity = redisLockOperation.findById(commentRedisRepository, commentId).orElseThrow(()-> new DataValidationException(String.format("Comment with id %s not found", commentId)));
        if (commentRedisEntity != null){
        redisLockOperation.deleteById(commentRedisRepository, commentId);
        if (commentRedisEntity.getAuthorComment() != null){
            CompletableFuture<UserDto> author = authorRedisService.deleteAuthor(commentRedisEntity.getAuthorComment().getId());}
        }
    }

    @Override
    public void incrementComment(Long commentId) {
        redisLockOperation.findById(commentRedisRepository, commentId).ifPresent(comment -> {comment.setLikeCount(comment.getLikeCount() + 1);
            redisLockOperation.updateOrSave(commentRedisRepository, comment, comment.getId());});
    }

    @Override
    public void decrementComment(Long commentId) {
        redisLockOperation.findById(commentRedisRepository, commentId).ifPresent(comment -> {comment.setLikeCount(comment.getLikeCount() - 1);
            redisLockOperation.updateOrSave(commentRedisRepository, comment, comment.getId());});
    }
}
