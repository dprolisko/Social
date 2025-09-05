package com.dprol.post_service.validator.like;

import com.dprol.post_service.repository.CommentRepository;
import com.dprol.post_service.repository.like.PostLikeRepository;
import com.dprol.post_service.exception.DataValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class LikeValidatorImpl implements LikeValidator {

    private final PostLikeRepository postLikeRepository;

    private final CommentRepository commentRepository;

    @Override
    public void validatePostLike(Long postLikeId) {
        boolean isExist = postLikeRepository.existsById(postLikeId);
        if (!isExist) {
            throw new DataValidationException("Like " + postLikeId + " is not exist");
        }
    }

    @Override
    public void validateCommentLike(Long commentLikeId) {
        boolean isExist = commentRepository.existsById(commentLikeId);
        if (!isExist) {
            throw new DataValidationException("Comment " + commentLikeId + " is not exist");
        }
    }
}
