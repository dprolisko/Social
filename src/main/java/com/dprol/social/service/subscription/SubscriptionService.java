package com.dprol.social.service.subscription;

import com.dprol.social.dto.SubscriptionDto;
import com.dprol.social.dto.user.UserDto;
import com.dprol.social.dto.user.UserFilterDto;

import java.util.List;

public interface SubscriptionService {

    void followUser(SubscriptionDto subscriptionDto);

    void unfollowUser(SubscriptionDto subscriptionDto);

    List<UserDto> getFollowers(Long followeeId, UserFilterDto userFilterDto);

    List<UserDto> getFollowings(Long followerId, UserFilterDto userFilterDto);

    int getFollowersCount(Long followeeId);

    int getFollowingsCount(Long followerId);
}
