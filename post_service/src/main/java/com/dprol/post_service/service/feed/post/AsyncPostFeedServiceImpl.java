package com.dprol.post_service.service.feed.post;

import com.dprol.post_service.config.UserContextConfig;
import com.dprol.post_service.dto.feed.PostFeedDto;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Async("FeedTaskExecutor")

public class AsyncPostFeedServiceImpl implements AsyncPostFeedService {

    private final PostFeedService postFeedService;

    private final UserContextConfig userContextConfig;

    @Override
    public CompletableFuture<PostFeedDto> getPostFeedAsync(Long postId, Long userId) {
        userContextConfig.setUserId(userId);
        PostFeedDto postFeedDto = postFeedService.getPostFeed(postId);
        return CompletableFuture.completedFuture(postFeedDto);
    }
}
