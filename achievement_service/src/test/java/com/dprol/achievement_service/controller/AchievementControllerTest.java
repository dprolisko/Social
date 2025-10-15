package com.dprol.achievement_service.controller;

import com.dprol.achievement_service.dto.AchievementDto;
import com.dprol.achievement_service.service.achievement.AchievementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AchievementControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AchievementService achievementService;

    @InjectMocks
    private AchievementController achievementController;

    private ObjectMapper objectMapper;

    private AchievementDto achievementDto;

    private AchievementDto achievementDto2;

    @BeforeEach
    void setUp() {
    objectMapper = new ObjectMapper();
    mockMvc = MockMvcBuilders.standaloneSetup(achievementController).build();
    achievementDto = AchievementDto.builder().id(1L).title("First Achievement").build();
    achievementDto2 = AchievementDto.builder().id(2L).title("Champion").build();
    }

    @Test
    void getAchievementById_ShouldReturnDto() throws Exception {

        Mockito.when(achievementService.findAchievementById(1L)).thenReturn(achievementDto);

        mockMvc.perform(get("/achievement/get/{achievementId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("First Achievement"));
    }

    @Test
    void getAchievementByTitle_ShouldReturnDto() throws Exception {

        Mockito.when(achievementService.findAchievementByTitle(eq("Champion"))).thenReturn(achievementDto2);

        mockMvc.perform(get("/achievement/getByTitle")
                        .param("title", "Champion"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.title").value("Champion"));
    }
}
