package com.dprol.post_service.service.like;

import com.dprol.post_service.dto.like.PostLikeDto;

public interface PostLikeService {

    PostLikeDto addPostLike(Long postId, Long userId);

    void deletePostLike(Long postLikeId, Long userId);
}
