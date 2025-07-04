package com.dprol.social.dto.goal;

import com.dprol.social.entity.goal.GoalStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class GoalInvitationFilterDto {

    private Long inviterId;

    private Long invitedId;

    private GoalStatus status;

    private String inviterUsername;

    private String invitedUsername;
}
