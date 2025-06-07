package com.dprol.social.validator.subscription;

import com.dprol.social.dto.SubscriptionDto;
import com.dprol.social.exception.DataValidationException;
import com.dprol.social.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component

public class SubscriptionValidatorImpl implements SubscriptionValidator {

    private final SubscriptionRepository subscriptionRepository;

    @Override
    public void validateFollowerAndFollowingId(SubscriptionDto subscriptionDto) {
        boolean isFollower = subscriptionDto.getFollowerId() == subscriptionDto.getFollowingId();
        if (!isFollower) {
            throw new DataValidationException("Follower and following has same ids");
        }
    }

    @Override
    public void validateSubscription(SubscriptionDto subscriptionDto) {
        boolean isExists = subscriptionRepository.existsByFollowerId(subscriptionDto.getFollowerId(), subscriptionDto.getFollowingId());
        if (!isExists) {
            throw new DataValidationException("Subscription with id " + subscriptionDto.getFollowerId() + " already exists");
        }
    }
}
