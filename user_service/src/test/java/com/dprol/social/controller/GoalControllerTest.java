package com.dprol.social.controller;

import com.dprol.social.config.UserContextConfig;
import com.dprol.social.controller.goal.GoalController;
import com.dprol.social.dto.goal.GoalDto;
import com.dprol.social.dto.goal.GoalFilterDto;
import com.dprol.social.entity.goal.Goal;
import com.dprol.social.service.goal.goal.GoalService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GoalController.class)
class GoalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GoalService goalService;

    @MockBean
    private UserContextConfig userContextConfig;

    private final Long testUserId = 1L;
    private final Long testGoalId = 100L;
    private final GoalDto goalDto = new GoalDto(/* заполните данными */);
    private final Goal goal = new Goal(/* заполните данными */);

    @Test
    void createGoal_ShouldReturnCreated() throws Exception {
        when(goalService.createGoal(any(GoalDto.class), eq(testUserId)))
                .thenReturn(goalDto);

        mockMvc.perform(post("/goal")
                        .param("userId", testUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(goalDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(goalDto.getId()));
    }

    @Test
    void getListGoals_ShouldReturnList() throws Exception {
        List<GoalDto> goals = Collections.singletonList(goalDto);
        when(goalService.getListGoals(eq(testUserId), any(GoalFilterDto.class)))
                .thenReturn(goals);

        mockMvc.perform(get("/goal/list")
                        .param("userId", testUserId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value(goalDto.getTitle()));
    }

    @Test
    void findGoalById_ShouldReturnGoal() throws Exception {
        when(goalService.findGoalById(testGoalId))
                .thenReturn(goal);

        mockMvc.perform(get("/goal/{goalId}", testGoalId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(goal.getId()));
    }

    @Test
    void updateGoal_ShouldReturnUpdatedGoal() throws Exception {
        // Настройка mock для контекста пользователя
        when(userContextConfig.getUserId()).thenReturn(testUserId);
        when(goalService.updateGoal(any(GoalDto.class), eq(testUserId), eq(testGoalId)))
                .thenReturn(goalDto);

        mockMvc.perform(put("/goal/{goalId}", testGoalId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(goalDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(goalDto.getDescription()));
    }

    @Test
    void findGoalById_NotFound_ShouldReturn404() throws Exception {
        when(goalService.findGoalById(testGoalId))
                .thenThrow(new EntityNotFoundException("Goal not found"));

        mockMvc.perform(get("/goal/{goalId}", testGoalId))
                .andExpect(status().isNotFound());
    }

    @Test
    void createGoal_MissingUserId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/goal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(goalDto)))
                .andExpect(status().isBadRequest());
    }
}
