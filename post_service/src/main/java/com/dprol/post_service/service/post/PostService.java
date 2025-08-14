package com.dprol.post_service.service.post;

import com.dprol.post_service.dto.PostDto;
import com.dprol.post_service.entity.Post;

import java.util.List;

public interface PostService {

    PostDto createPost(PostDto postDto);

    void deletePost(Long postId);

    List<PostDto> getListPostsByUserId(Long userId);

    Post findPostById(Long postId);

    PostDto publishPost(Long postId);

    PostDto updatePost(Long postId);

    List<Long> getListPostIdsNotVerified();
}
