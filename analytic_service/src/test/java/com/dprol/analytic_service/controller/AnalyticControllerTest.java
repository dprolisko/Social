package com.dprol.analytic_service.controller;

import com.dprol.analytic_service.dto.AnalyticDto;
import com.dprol.analytic_service.entity.Interval;
import com.dprol.analytic_service.entity.Type;
import com.dprol.analytic_service.service.AnalyticService;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AnalyticControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private AnalyticService analyticService;

    @InjectMocks
    private AnalyticController analyticController;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(analyticController).build();
    }

    @Test
    void getAnalytics_WithInterval_ShouldReturnList() throws Exception {
        AnalyticDto dto1;
        dto1 = AnalyticDto.builder().id(1L).build();
        AnalyticDto dto2;
        dto2 = AnalyticDto.builder().id(2L).build();

        Mockito.when(analyticService.getAllAnalytics(
                eq(100L),
                eq(Type.POST_LIKE),
                eq(Interval.day),
                any(),
                any()
        )).thenReturn(List.of(dto1, dto2));

        mockMvc.perform(get("/analytic/get")
                        .param("receiverId", "100")
                        .param("type", "POST_LIKE")
                        .param("interval", "day"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }
}
