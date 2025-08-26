package com.dprol.post_service.validator;

import com.dprol.post_service.entity.Post;

public interface PostValidator {

    void validatePostById(Long postId);

    void validatePostByVereficationStatus(Post post);

    void validatePublicationPost(Post post);
}
