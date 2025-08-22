package com.dprol.post_service.mapper;

import com.dprol.post_service.dto.CommentDto;
import com.dprol.post_service.entity.Comment;
import com.dprol.post_service.kafka.event.CommentEvent;
import com.dprol.post_service.redis.entity.CommentRedisEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {
    CommentDto toDto(Comment comment);
    Comment toEntity(CommentDto commentDto);
    CommentRedisEntity toCommentEvent(CommentEvent commentEvent);
}
