package com.dprol.post_service.controller;

import com.dprol.post_service.dto.CommentDto;
import com.dprol.post_service.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor

public class CommentController {
    private final CommentService commentService;

    @PostMapping("/create")
    public CommentDto createComment(@RequestBody CommentDto commentDto) {
        return commentService.createComment(commentDto);
    }

    @PutMapping("update")
    public CommentDto updateComment(@RequestBody CommentDto commentDto) {
        return commentService.updateComment(commentDto.getId());
    }

    @DeleteMapping("/delete/{commentId}")
    public void deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }

    @GetMapping("/getComments/{userId}")
    public List<CommentDto> getCommentsByUserId(@PathVariable Long userId) {
        return commentService.getListCommentByUserId(userId);
    }
}
