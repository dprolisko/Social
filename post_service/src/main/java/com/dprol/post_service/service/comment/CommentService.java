package com.dprol.post_service.service.comment;

import com.dprol.post_service.dto.CommentDto;
import com.dprol.post_service.entity.Comment;

import java.util.List;

public interface CommentService {

    CommentDto createComment(CommentDto commentDto);

    void deleteComment(Long commentId);

    CommentDto updateComment(Long commentId);

    List<CommentDto> getListCommentByUserId(Long userId);

    Comment findCommentById(Long commentId);

    void verifyComment(List<Comment> comments);
}
