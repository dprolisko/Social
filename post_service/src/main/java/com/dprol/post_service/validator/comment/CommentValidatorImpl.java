package com.dprol.post_service.validator.comment;

import com.dprol.post_service.entity.Comment;
import com.dprol.post_service.repository.CommentRepository;
import com.dprol.post_service.exception.DataValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class CommentValidatorImpl implements CommentValidator {

    private final CommentRepository commentRepository;

    @Override
    public void validateCommentById(Long commentId) {
        boolean isExist = commentRepository.existsById(commentId);
        if (!isExist) {
            throw new DataValidationException("Comment with id " + commentId + " does not exist");
        }
    }

    @Override
    public void validateCommentByVerificationStatus(Comment comment) {
        boolean isExist = commentRepository.existsById(comment.getId());
        if (!isExist) {
            throw new DataValidationException("Comment with id " + comment.getId() + " does not exist");
        }
    }

    @Override
    public void validatePublicationComment(Comment comment) {
        if (comment.isPublished()){
            throw new DataValidationException("Comment is already published");
        }
    }
}
