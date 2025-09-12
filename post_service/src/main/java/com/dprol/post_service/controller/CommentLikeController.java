package com.dprol.post_service.controller;

import com.dprol.post_service.dto.like.CommentLikeDto;
import com.dprol.post_service.service.like.CommentLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("commentLike")
@RequiredArgsConstructor

public class CommentLikeController {
    private final CommentLikeService commentLikeService;

    @PostMapping("{commentLikeId}")
    public CommentLikeDto addCommentLike(Long commentLikeId, Long userId) {
        return commentLikeService.addCommentLike(commentLikeId, userId);
    }

    @DeleteMapping("delete/{commentLikeId}")
    public void deleteCommentLike(Long commentLikeId, Long userId) {
        commentLikeService.deleteCommentLike(commentLikeId, userId);
    }
}
