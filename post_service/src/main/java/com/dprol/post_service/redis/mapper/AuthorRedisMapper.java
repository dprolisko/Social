package com.dprol.post_service.redis.mapper;

import com.dprol.post_service.dto.UserDto;
import com.dprol.post_service.redis.entity.AuthorRedisEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthorRedisMapper {

    AuthorRedisEntity toCacheEntity(UserDto userDto);
    UserDto toDto(AuthorRedisEntity authorRedisEntity);
}
