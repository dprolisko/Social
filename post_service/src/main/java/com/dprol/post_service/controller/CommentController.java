package com.dprol.post_service.controller;

import com.dprol.post_service.dto.CommentDto;
import com.dprol.post_service.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("comment")
@RequiredArgsConstructor

public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public CommentDto createComment(@PathVariable CommentDto commentDto) {
        return commentService.createComment(commentDto);
    }

    @PutMapping
    public CommentDto updateComment(@PathVariable CommentDto commentDto) {
        return commentService.updateComment(commentDto.getId());
    }

    @DeleteMapping("{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }

    @GetMapping("{userId}")
    public List<CommentDto> getCommentsByUserId(@PathVariable Long userId) {
        return commentService.getListCommentByUserId(userId);
    }
}
