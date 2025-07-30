package com.dprol.post_service.mapper;

import com.dprol.post_service.dto.PostDto;
import com.dprol.post_service.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {
    PostDto toDto(Post post);
    Post toEntity(PostDto postDto);
}
