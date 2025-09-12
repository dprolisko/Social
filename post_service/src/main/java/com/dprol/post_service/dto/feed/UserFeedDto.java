package com.dprol.post_service.dto.feed;

import com.dprol.post_service.dto.UserProfileDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserFeedDto {

    private Long userId;

    private String userName;

    private UserProfileDto userProfile;
}
