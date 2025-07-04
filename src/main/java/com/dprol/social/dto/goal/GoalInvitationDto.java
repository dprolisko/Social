package com.dprol.social.dto.goal;

import com.dprol.social.dto.user.UserDto;
import com.dprol.social.entity.goal.GoalStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class GoalInvitationDto {

    private Long id;

    @NotNull
    private Long inviterId;

    @NotNull
    private Long invitedId;

    @NotNull
    private Long goalId;

    @NotNull
    private GoalStatus status;
}
