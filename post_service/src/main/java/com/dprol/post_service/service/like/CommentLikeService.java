package com.dprol.post_service.service.like;

import com.dprol.post_service.dto.like.CommentLikeDto;

public interface CommentLikeService {

    CommentLikeDto addCommentLike(Long commentLikeId, Long userId);

    void deleteCommentLike(Long commentLikeId, Long userId);
}
