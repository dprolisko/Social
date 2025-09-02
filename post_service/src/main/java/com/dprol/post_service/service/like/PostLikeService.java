package com.dprol.post_service.service.like;

import com.dprol.post_service.dto.like.PostLikeDto;

public interface PostLikeService {

    PostLikeDto addPostLike(PostLikeDto postLikeDto);

    void deletePostLike(Long postLikeId);
}
