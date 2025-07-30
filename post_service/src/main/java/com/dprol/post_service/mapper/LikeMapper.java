package com.dprol.post_service.mapper;

import com.dprol.post_service.dto.LikeDto;
import com.dprol.post_service.entity.Like;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LikeMapper {
    LikeDto toDto(Like like);
    Like toEntity(LikeMapper likeDto);
}
