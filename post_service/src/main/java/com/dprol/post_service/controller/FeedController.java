package com.dprol.post_service.controller;

import com.dprol.post_service.config.UserContextConfig;
import com.dprol.post_service.dto.feed.FeedDto;
import com.dprol.post_service.service.feed.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("feed")
@RequiredArgsConstructor

public class FeedController {

    private final FeedService feedService;

    private final UserContextConfig userContextConfig;

    @GetMapping
    public List<FeedDto> getFeeds(Pageable pageable) {
        Long userId = userContextConfig.getUserId();
        return feedService.getNewsFeed(userId, pageable);
    }
}
