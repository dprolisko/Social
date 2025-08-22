package com.dprol.post_service.redis.service;

import com.dprol.post_service.config.UserContextConfig;
import com.dprol.post_service.dto.UserDto;
import com.dprol.post_service.redis.entity.AuthorRedisEntity;
import com.dprol.post_service.redis.mapper.AuthorRedisMapper;
import com.dprol.post_service.redis.repository.AuthorRedisRepository;
import com.dprol.post_service.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor

public class AuthorRedisServiceIpml implements AuthorRedisService {

    private final AuthorRedisRepository authorRedisRepository;

    private final RedisLockOperation redisLockOperation;

    private final UserContextConfig userContextConfig;

    private final AuthorRedisMapper authorRedisMapper;

    private final UserService userService;

    @Override
    public CompletableFuture<UserDto> saveAuthor(AuthorRedisEntity author) {
        userContextConfig.setUserId(author.getId());
        UserDto userDto = userService.getUserById(author.getId());
        AuthorRedisEntity authorRedisEntity = authorRedisMapper.toCacheEntity(userDto);
        authorRedisEntity = redisLockOperation.updateOrSave(authorRedisRepository, authorRedisEntity, authorRedisEntity.getId());
        return CompletableFuture.completedFuture(authorRedisMapper.toDto(authorRedisEntity));
    }

    @Override
    public CompletableFuture<UserDto> getAuthorByCache(AuthorRedisEntity author) {
        userContextConfig.setUserId(author.getId());
        UserDto userDto = userService.getUserById(author.getId());
        return CompletableFuture.completedFuture(userDto);
    }

    @Override
    public CompletableFuture<UserDto> deleteAuthor(Long authorId) {
        userContextConfig.setUserId(authorId);
        UserDto userDto = userService.getUserById(authorId);
        AuthorRedisEntity authorRedisEntity = authorRedisMapper.toCacheEntity(userDto);
        redisLockOperation.deleteById(authorRedisRepository, authorId);
        return CompletableFuture.completedFuture(userDto);
    }
}
