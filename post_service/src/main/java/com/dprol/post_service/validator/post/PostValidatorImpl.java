package com.dprol.post_service.validator.post;

import com.dprol.post_service.entity.Post;
import com.dprol.post_service.repository.PostRepository;
import exception.DataValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class PostValidatorImpl implements PostValidator {

    private final PostRepository postRepository;

    @Override
    public void validatePostById(Long postId) {
        boolean isExist = postRepository.existsById(postId);
        if (!isExist) {
            throw new DataValidationException("Post with id " + postId + " does not exist");
        }
    }

    @Override
    public void validatePostByVerificationStatus(Post post) {
        boolean isExist = postRepository.existsById(post.getId());
        if (!isExist) {
            throw new DataValidationException("Post with id " + post.getId() + " does not exist");
        }
    }

    @Override
    public void validatePublicationPost(Post post) {
        if (post.isPublished()){
            throw new DataValidationException("Post is already published");
        }
    }
}
