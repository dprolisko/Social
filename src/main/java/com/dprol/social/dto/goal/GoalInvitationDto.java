package com.dprol.social.dto.goal;

import com.dprol.social.dto.user.UserDto;
import com.dprol.social.entity.goal.GoalStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class GoalInvitationDto {

    private Long id;

    @NotBlank
    private UserDto inviterId;

    @NotBlank
    private UserDto invitedId;

    @NotBlank
    private GoalDto goalId;

    @NotBlank
    private GoalStatus status;
}
