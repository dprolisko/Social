package com.dprol.post_service.scheduler;

import com.dprol.post_service.redis.publisher.UserBanPublisher;
import com.dprol.post_service.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class UserBannerScheduler {

    private final PostService postService;

    private final UserBanPublisher userBanPublisher;

    @Scheduled(cron = "${post.user-ban.scheduler.cron}")
    @Async("BanUsersExecutor")
    public void banUser(){
        List<Long> bannedUsers = postService.findAllAuthorIdsWithNotVerifiedPosts().stream()
                .collect(Collectors.groupingBy(group -> group, Collectors.counting()))
                .entrySet().stream()
                .filter(filt -> filt.getValue()>3)
                .map(Map.Entry::getKey).toList();
    }
}
