package com.dprol.social.mapper;

import com.dprol.social.dto.RatingDto;
import com.dprol.social.entity.Rating;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RatingMapper {
    RatingDto toDto(Rating rating);
    Rating toEntity(RatingDto ratingDto);
}
