package com.dprol.post_service.service.feed.post;

import com.dprol.post_service.dto.feed.PostFeedDto;

import java.util.concurrent.CompletableFuture;

public interface AsyncPostFeedService {

    CompletableFuture<PostFeedDto> getPostFeedAsync(Long postId, Long userId);
}
