package com.dprol.post_service.mapper.like;

import com.dprol.post_service.dto.like.CommentLikeDto;
import com.dprol.post_service.entity.like.CommentLike;
import com.dprol.post_service.kafka.event.Status;
import com.dprol.post_service.kafka.event.like.CommentLikeEvent;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentLikeMapper {
    CommentLikeDto toDto(CommentLike commentLike);
    CommentLike toEntity(CommentLikeDto commentDto);
    CommentLikeEvent toKafkaEvent(CommentLike commentLike, Status status);
}
