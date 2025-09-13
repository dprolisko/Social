package com.dprol.post_service.service.feed.comment;

import com.dprol.post_service.dto.feed.CommentFeedDto;

import java.util.List;

public interface CommentFeedService {

    List<CommentFeedDto> getCommentFeed(Long postId);
}
