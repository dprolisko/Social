package com.dprol.social.validator;

import com.dprol.social.dto.SubscriptionDto;

public interface SubscriptionValidator {

    void validateFollowerAndFollowingId(SubscriptionDto subscriptionDto);

    void validateSubscription(SubscriptionDto subscriptionDto);
}
