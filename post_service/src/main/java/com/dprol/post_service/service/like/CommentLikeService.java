package com.dprol.post_service.service.like;

import com.dprol.post_service.dto.like.CommentLikeDto;

public interface CommentLikeService {

    CommentLikeDto addCommentLike(CommentLikeDto commentLikeDto);

    void deleteCommentLike(Long commentLikeId);
}
