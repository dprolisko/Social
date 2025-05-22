package com.dprol.social.service;

import com.dprol.social.dto.SubscriptionDto;
import com.dprol.social.dto.UserDto;
import com.dprol.social.mapper.UserMapper;
import com.dprol.social.repository.SubscriptionRepository;
import com.dprol.social.validator.SubscriptionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    private final UserMapper userMapper;

    private final SubscriptionValidator subscriptionValidator;

    @Override
    public void followUser(SubscriptionDto subscriptionDto) {

    }

    @Override
    public void unfollowUser(SubscriptionDto subscriptionDto) {

    }

    @Override
    public List<UserDto> getFollowers(Long followeeId) {
        return List.of();
    }

    @Override
    public List<UserDto> getFollowings(Long followerId) {
        return List.of();
    }

    @Override
    public int getFollowersCount(Long followeeId) {
        return 0;
    }

    @Override
    public int getFollowingsCount(Long followerId) {
        return 0;
    }
}
