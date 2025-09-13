package com.dprol.post_service.mapper;

import com.dprol.post_service.dto.feed.CommentFeedDto;
import com.dprol.post_service.dto.feed.FeedDto;
import com.dprol.post_service.dto.feed.PostFeedDto;
import com.dprol.post_service.redis.entity.PostRedisEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FeedMapper {
    FeedDto toPostDto(PostFeedDto postFeedDto, List<CommentFeedDto> commentFeedDtoList);
    FeedDto toCacheDto(PostRedisEntity postRedisEntity);
}
