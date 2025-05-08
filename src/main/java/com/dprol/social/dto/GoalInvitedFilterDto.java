package com.dprol.social.dto;

import com.dprol.social.entity.GoalStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class GoalInvitedFilterDto {

    private Long inviterId;

    private Long invitedId;

    private GoalStatus status;

    private String inviterUsername;

    private String invitedUsername;
}
