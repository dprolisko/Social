package com.dprol.social.service.subscription;

import com.dprol.social.dto.SubscriptionDto;
import com.dprol.social.dto.user.UserDto;
import com.dprol.social.dto.user.UserFilterDto;
import com.dprol.social.event.SubscriptionEvent;
import com.dprol.social.mapper.user.UserMapper;
import com.dprol.social.publisher.SubscriptionPublisher;
import com.dprol.social.repository.SubscriptionRepository;
import com.dprol.social.service.user.filter.UserFilterService;
import com.dprol.social.validator.subscription.SubscriptionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor

public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    private final UserMapper userMapper;

    private final SubscriptionValidator subscriptionValidator;

    private final UserFilterService userFilterService;

    private final SubscriptionPublisher subscriptionPublisher;

    @Override
    public void followUser(SubscriptionDto subscriptionDto) {
        subscriptionValidator.validateSubscription(subscriptionDto);
        subscriptionValidator.validateFollowerAndFollowingId(subscriptionDto);
        subscriptionRepository.followUser(subscriptionDto.getFollowerId(), subscriptionDto.getFollowingId());
        subscriptionPublisher.publisher(new SubscriptionEvent(subscriptionDto.getFollowingId(), subscriptionDto.getFollowerId(), LocalDateTime.now()));
    }

    @Override
    public void unfollowUser(SubscriptionDto subscriptionDto) {
        subscriptionValidator.validateFollowerAndFollowingId(subscriptionDto);
        subscriptionRepository.unfollowUser(subscriptionDto.getFollowerId(), subscriptionDto.getFollowingId());
    }

    @Override
    public List<UserDto> getFollowers(Long followeeId, UserFilterDto userFilterDto) {
        return userFilterService.filterUsers(subscriptionRepository.findByFolloweesId(followeeId), userFilterDto)
                .map(userMapper::toDto).toList();
    }

    @Override
    public List<UserDto> getFollowings(Long followerId, UserFilterDto userFilterDto) {
        return userFilterService.filterUsers(subscriptionRepository.findByFollowersId(followerId), userFilterDto)
                .map(userMapper::toDto).toList();
    }

    @Override
    public int getFollowersCount(Long followerId) {
        return subscriptionRepository.findByFollowerId(followerId);
    }

    @Override
    public int getFollowingsCount(Long followeeId) {
        return subscriptionRepository.findByFolloweeId(followeeId);
    }
}
