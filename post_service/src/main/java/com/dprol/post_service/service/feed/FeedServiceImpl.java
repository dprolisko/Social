package com.dprol.post_service.service.feed;

import com.dprol.post_service.config.UserContextConfig;
import com.dprol.post_service.dto.UserDto;
import com.dprol.post_service.dto.feed.CommentFeedDto;
import com.dprol.post_service.dto.feed.FeedDto;
import com.dprol.post_service.dto.feed.PostFeedDto;
import com.dprol.post_service.mapper.FeedMapper;
import com.dprol.post_service.redis.entity.CommentRedisEntity;
import com.dprol.post_service.redis.entity.FeedRedisEntity;
import com.dprol.post_service.redis.entity.PostRedisEntity;
import com.dprol.post_service.redis.service.FeedRedisService;
import com.dprol.post_service.repository.PostRepository;
import com.dprol.post_service.service.feed.comment.AsyncCommentFeedService;
import com.dprol.post_service.service.feed.post.AsyncPostFeedService;
import com.dprol.post_service.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor

public class FeedServiceImpl implements FeedService {

    private final AsyncCommentFeedService asyncCommentFeedService;

    private final AsyncPostFeedService asyncPostFeedService;

    private final UserService userService;

    private final UserContextConfig userContextConfig;

    private final PostRepository postRepository;

    private final FeedRedisService feedRedisService;

    private final FeedMapper feedMapper;

    @Override
    @Transactional(readOnly = true)
    public List<FeedDto> getNewsFeed(Long userId, Pageable pageable) {

        UserDto user = userService.getUserById(userId);
        List<FeedDto> cachePosts = getFromRedisCache(userId, pageable);

        if (cachePosts != null) {
            return cachePosts;
        }

        return getFromDataBase(user.getSubscriptions(), pageable);
    }

    private List<FeedDto> getFromRedisCache(long userId, Pageable pageable) {

        FeedRedisEntity feedRedis = feedRedisService.getPostInFeed(userId);

        if (feedRedis == null || feedRedis.getFeedPosts().isEmpty()) {
            return null;
        }

        NavigableSet<PostRedisEntity> posts = feedRedis.getFeedPosts();

        if (pageable.getOffset() + pageable.getPageSize() > posts.size()) {
            return null;
        }

        List<PostRedisEntity> caches = getPage(posts, pageable);
        List<FeedDto> out = new ArrayList<>();

        for (PostRedisEntity cache : caches) {
            FeedDto post = getFeedPublicationFromCache(cache);
            out.add(post);
        }

        return out;
    }

    private FeedDto getFeedPublicationFromCache(PostRedisEntity cache) {
        for (CommentRedisEntity commentCache : cache.getComments()) {
            if (commentCache.getAuthorComment() == null) {
                return getFeedPublicationFromDataBase(cache.getId());
            }
        }

        if (cache.getAuthorPost() == null) {
            return getFeedPublicationFromDataBase(cache.getId());
        }

        return feedMapper.toCacheDto(cache);
    }

    private List<FeedDto> getFromDataBase(List<Long> subscriberIds, Pageable pageable) {

        return postRepository.findFeedPost(subscriberIds, pageable).stream()
                .map(this::getFeedPublicationFromDataBase)
                .toList();
    }

    private FeedDto getFeedPublicationFromDataBase(long postId) {

        long currentUserId = userContextConfig.getUserId();
        CompletableFuture<PostFeedDto> post = asyncPostFeedService.getPostFeedAsync(postId, currentUserId);
        CompletableFuture<List<CommentFeedDto>> comments = asyncCommentFeedService.getCommentFeedAsync(postId, currentUserId);

        return post
                .thenCombine(comments, feedMapper::toPostDto)
                .join();
    }

    private List<PostRedisEntity> getPage(NavigableSet<PostRedisEntity> posts, Pageable pageable) {

        return posts.stream()
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .toList();
    }
}

