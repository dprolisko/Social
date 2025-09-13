package com.dprol.post_service.service.feed;

import com.dprol.post_service.dto.feed.FeedDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FeedService {

    List<FeedDto> getNewsFeed(Long userId, Pageable pageable);
}
