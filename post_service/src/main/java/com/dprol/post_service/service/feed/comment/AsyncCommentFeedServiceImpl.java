package com.dprol.post_service.service.feed.comment;

import com.dprol.post_service.config.UserContextConfig;
import com.dprol.post_service.dto.feed.CommentFeedDto;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Async("FeedTaskExecutor")

public class AsyncCommentFeedServiceImpl implements AsyncCommentFeedService {

    private final CommentFeedService commentFeedService;

    private final UserContextConfig userContextConfig;

    @Override
    public CompletableFuture<List<CommentFeedDto>> getCommentFeedAsync(Long postId, Long userId) {
        userContextConfig.setUserId(userId);
        return CompletableFuture.completedFuture(commentFeedService.getCommentFeed(postId));
    }
}
