package com.dprol.social.service.subscription;

import com.dprol.social.dto.SubscriptionDto;
import com.dprol.social.dto.user.UserDto;

import java.util.List;

public interface SubscriptionService {

    void followUser(SubscriptionDto subscriptionDto);

    void unfollowUser(SubscriptionDto subscriptionDto);

    List<UserDto> getFollowers(Long followeeId);

    List<UserDto> getFollowings(Long followerId);

    int getFollowersCount(Long followeeId);

    int getFollowingsCount(Long followerId);
}
