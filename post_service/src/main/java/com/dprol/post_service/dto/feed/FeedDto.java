package com.dprol.post_service.dto.feed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class FeedDto {

    private PostFeedDto postFeed;

    private List<CommentFeedDto> commentFeed;
}
