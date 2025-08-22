package com.dprol.post_service.redis.service;

import com.dprol.post_service.dto.UserDto;
import com.dprol.post_service.redis.entity.AuthorRedisEntity;

import java.util.concurrent.CompletableFuture;

public interface AuthorRedisService {

    CompletableFuture<UserDto> saveAuthor(AuthorRedisEntity author);

    CompletableFuture<UserDto> getAuthorByCache(AuthorRedisEntity author);

    CompletableFuture<UserDto> deleteAuthor(Long authorId);
}
