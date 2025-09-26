package com.dprol.post_service.controller;

import com.dprol.post_service.dto.like.PostLikeDto;
import com.dprol.post_service.service.like.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/postLike")
@RequiredArgsConstructor

public class PostLikeController {
    private final PostLikeService postLikeService;

    @PostMapping("/add/{postLikeId}")
    public PostLikeDto addPostLike(Long postLikeId, Long userId) {
        return postLikeService.addPostLike(postLikeId, userId);
    }

    @DeleteMapping("/delete/{postLikeId}")
    public void deletePostLike(Long postLikeId, Long userId) {
        postLikeService.deletePostLike(postLikeId, userId);
    }
}
