package com.dprol.post_service.validator;

public interface PostValidator {

    void validatePostById(Long postId);

    void validatePostByVereficationStatus()
}
