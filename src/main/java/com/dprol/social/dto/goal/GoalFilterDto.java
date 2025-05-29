package com.dprol.social.dto.goal;

import com.dprol.social.entity.goal.GoalStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class GoalFilterDto {

    private Long goalId;

    private String goalName;

    private GoalStatus goalStatus;

    private LocalDateTime deadline;
}
