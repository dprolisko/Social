package com.dprol.post_service.scheduler;

import com.dprol.post_service.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class PostCorrector {

    private final PostService postService;

    @Scheduled(cron = "${post.correcter.scheduler.cron}")
    public void check(){
        postService.correctPost();
    }
}
