package com.dprol.post_service.mapper.like;

import com.dprol.post_service.dto.like.PostLikeDto;
import com.dprol.post_service.entity.like.PostLike;
import com.dprol.post_service.kafka.event.Status;
import com.dprol.post_service.kafka.event.like.PostLikeEvent;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostLikeMapper {
    PostLikeDto toDto(PostLike postLike);
    PostLike toEntity(PostLikeDto likeDto);
    PostLikeEvent toKafkaEvent(PostLike postLike, Status status);
}
