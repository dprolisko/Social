package com.dprol.post_service.service.feed.post;

import com.dprol.post_service.dto.feed.PostFeedDto;
import com.dprol.post_service.entity.Post;
import com.dprol.post_service.exception.PostNotFoundException;
import com.dprol.post_service.mapper.PostMapper;
import com.dprol.post_service.repository.PostRepository;
import com.dprol.post_service.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class PostFeedServiceImpl implements PostFeedService {

    private final PostRepository postRepository;

    private final PostMapper postMapper;

    private final UserService userService;

    @Override
    public PostFeedDto getPostFeed(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("Post not found"));
        userService.getUserById(post.getAuthorId());
        return postMapper.toPostFeedDto(post);
    }
}
