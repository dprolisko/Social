package com.dprol.social.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class SubscriptionDto {

    private Long FollowerId;

    private Long FollowingId;
}
