package com.dprol.achievement_service.controller;

import com.dprol.achievement_service.dto.AchievementProgressDto;
import com.dprol.achievement_service.service.achievement_progress.AchievementProgressService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AchievementProgressControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AchievementProgressService achievementProgressService;

    @InjectMocks
    private AchievementProgressController achievementProgressController;

    private ObjectMapper objectMapper;

    private AchievementProgressDto achievementProgressDto1;

    private AchievementProgressDto achievementProgressDto2;

    @BeforeEach
    void setUp() {
        achievementProgressDto1 = AchievementProgressDto.builder().id(1L).build();
        achievementProgressDto2 = AchievementProgressDto.builder().id(2L).build();
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(achievementProgressController).build();
    }

    @Test
    void getAchievementProgress_ShouldReturnListOfDtos() throws Exception {

        Mockito.when(achievementProgressService.getAllAchievementProgress(eq(10L)))
                .thenReturn(List.of(achievementProgressDto1, achievementProgressDto2));

        mockMvc.perform(get("/achievementProgress/get/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }
}
