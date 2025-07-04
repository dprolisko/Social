package com.dprol.social.dto.goal;

import com.dprol.social.entity.goal.GoalStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class GoalFilterDto {

    private String goalName;

    private GoalStatus goalStatus;

    private LocalDateTime deadline;
}
