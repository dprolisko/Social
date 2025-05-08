package com.dprol.social.dto;

import com.dprol.social.entity.GoalStatus;
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
