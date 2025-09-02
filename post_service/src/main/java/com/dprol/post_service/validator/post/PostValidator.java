package com.dprol.post_service.validator.post;

import com.dprol.post_service.entity.Post;

public interface PostValidator {

    void validatePostById(Long postId);

    void validatePostByVerificationStatus(Post post);

    void validatePublicationPost(Post post);
}
