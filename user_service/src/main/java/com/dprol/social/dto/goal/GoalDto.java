package com.dprol.social.dto.goal;

import com.dprol.social.entity.goal.GoalStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class GoalDto {

    private Long id;

    @NotNull
    private List<Long> usersIds;

    @NotNull
    private String title;

    private String description;

    @NotNull
    private LocalDateTime deadline;

    @NotNull
    private GoalStatus status;

    public GoalDto(Long goalId, String learnJava, GoalStatus goalStatus) {
    }
}
