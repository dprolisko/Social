package com.dprol.post_service.validator.like;

public interface LikeValidator {

    void validatePostLike(Long postLikeId);

    void validateCommentLike(Long commentLikeId);
}
