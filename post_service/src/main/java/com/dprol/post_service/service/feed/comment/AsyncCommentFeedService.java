package com.dprol.post_service.service.feed.comment;

import com.dprol.post_service.dto.feed.CommentFeedDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface AsyncCommentFeedService {

    CompletableFuture<List<CommentFeedDto>> getCommentFeedAsync(Long postId, Long userId);
}
