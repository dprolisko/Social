package com.dprol.post_service.scheduler;

import com.dprol.post_service.entity.Post;
import com.dprol.post_service.repository.PostRepository;
import com.dprol.post_service.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor

public class SchedulerModerator {

    private final PostRepository postRepository;

    private final PostService postService;

    @Value("${post.moderator.max-list-size}")
    private int size;

    public void moderatePosts() {
        List<Post> posts = postRepository.findAllUncheckedPosts();
        if (posts.isEmpty()) {
            return;
        }
        List<List<Post>> moderatedPosts = ListUtils.partition(posts, size);
        moderatedPosts.forEach(postService::verifyPost);
    }
}
