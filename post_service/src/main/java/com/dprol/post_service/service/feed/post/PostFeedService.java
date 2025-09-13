package com.dprol.post_service.service.feed.post;

import com.dprol.post_service.dto.feed.PostFeedDto;

public interface PostFeedService {

    PostFeedDto getPostFeed(Long postId);
}
