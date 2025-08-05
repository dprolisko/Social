package com.dprol.social.controller;

import com.dprol.social.config.UserContextConfig;
import com.dprol.social.controller.goal.GoalController;
import com.dprol.social.dto.goal.GoalDto;
import com.dprol.social.dto.goal.GoalFilterDto;
import com.dprol.social.entity.goal.Goal;
import com.dprol.social.entity.goal.GoalStatus;
import com.dprol.social.service.goal.goal.GoalService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class GoalControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private GoalService goalService;

    @Mock
    private UserContextConfig userContextConfig;

    private Long testUserId = 1L;
    private Long testGoalId = 100L;
    private GoalDto goalDto;
    private Goal goal;

    @InjectMocks
    private GoalController goalController;

    @BeforeEach
    void setUp() {
        userContextConfig.setUserId(testUserId);
        goalDto = GoalDto.builder().id(1L).title("title").description("description").status(GoalStatus.active).usersIds(List.of(1L, 2L)).build();
        goal = Goal.builder().id(1L).title("title").createdAt(LocalDateTime.now()).deadline(LocalDateTime.now()).status(GoalStatus.planned).build();
        mockMvc = MockMvcBuilders.standaloneSetup(goalController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createGoal_ShouldReturnCreated() throws Exception {
        when(goalService.createGoal(any(GoalDto.class), eq(testUserId)))
                .thenReturn(goalDto);

        mockMvc.perform(post("/goal/create/{userId}", testUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(goalDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(goalDto.getId()));
    }

    @Test
    void getListGoals_ShouldReturnList() throws Exception {
        List<GoalDto> goals = Collections.singletonList(goalDto);
        when(goalService.getListGoals(eq(testUserId), any(GoalFilterDto.class)))
                .thenReturn(goals);

        mockMvc.perform(get("/goal/get/{userId}", testUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value(goalDto.getTitle()));
    }

    @Test
    void findGoalById_ShouldReturnGoal() throws Exception {
        when(goalService.findGoalById(testGoalId))
                .thenReturn(goal);

        mockMvc.perform(get("/goal/find/{goalId}", testGoalId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(goal.getId()));
    }
}
