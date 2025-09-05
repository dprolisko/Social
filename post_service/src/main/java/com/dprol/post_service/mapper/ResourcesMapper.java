package com.dprol.post_service.mapper;

import com.dprol.post_service.dto.ResourceDto;
import com.dprol.post_service.entity.Resources;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ResourcesMapper {
    ResourceDto toDto(Resources resources);
    Resources toEntity(ResourceDto resourceDto);
}
