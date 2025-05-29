package com.dprol.social.validator.subscription;

import com.dprol.social.dto.SubscriptionDto;

public interface SubscriptionValidator {

    void validateFollowerAndFollowingId(SubscriptionDto subscriptionDto);

    void validateSubscription(SubscriptionDto subscriptionDto);
}
