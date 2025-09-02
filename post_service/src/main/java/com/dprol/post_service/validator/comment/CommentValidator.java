package com.dprol.post_service.validator.comment;

import com.dprol.post_service.entity.Comment;

public interface CommentValidator {

    void validateCommentById(Long commentId);

    void validateCommentByVerificationStatus(Comment comment);

    void validatePublicationComment(Comment comment);
}
