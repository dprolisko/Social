package com.dprol.social.controller;

import com.dprol.social.config.UserContextConfig;
import com.dprol.social.controller.premium.PremiumController;
import com.dprol.social.dto.PremiumDto;
import com.dprol.social.service.premium.PremiumService;
import com.github.dockerjava.api.exception.ConflictException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PremiumController.class)
class PremiumControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PremiumService premiumService;

    @MockBean
    private UserContextConfig userContextConfig;

    private final Long testUserId = 1L;
    private PremiumDto premiumDto = new PremiumDto();
    private PremiumDto activatedPremiumDto = new PremiumDto();

    @Test
    void activatePremium_ShouldReturnCreated() throws Exception {
        // Настройка моков
        when(userContextConfig.getUserId()).thenReturn(testUserId);
        when(premiumService.activatePremium(eq(testUserId), any(PremiumDto.class)))
                .thenReturn(activatedPremiumDto);

        // Выполнение запроса и проверки
        mockMvc.perform(post("/premium")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(premiumDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(activatedPremiumDto.getPremiumId()))
                .andExpect(jsonPath("$.status").value(activatedPremiumDto.getPeriod()));

        // Проверка вызовов
        verify(premiumService).activatePremium(testUserId, premiumDto);
    }

    @Test
    void activatePremium_InvalidData_ShouldReturnBadRequest() throws Exception {
        // Создание невалидных данных
        PremiumDto invalidDto = new PremiumDto(); // Некорректные данные

        mockMvc.perform(post("/premium")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void activatePremium_Conflict_ShouldReturnConflict() throws Exception {
        when(userContextConfig.getUserId()).thenReturn(testUserId);
        when(premiumService.activatePremium(eq(testUserId), any(PremiumDto.class)))
                .thenThrow(new ConflictException("Premium already active"));

        mockMvc.perform(post("/premium")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(premiumDto)))
                .andExpect(status().isConflict());
    }

    @Test
    void activatePremium_UserNotFound_ShouldReturnNotFound() throws Exception {
        when(userContextConfig.getUserId()).thenReturn(testUserId);
        when(premiumService.activatePremium(eq(testUserId), any(PremiumDto.class)))
                .thenThrow(new EntityNotFoundException("User not found"));

        mockMvc.perform(post("/premium")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(premiumDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void activatePremium_ServiceError_ShouldReturnInternalServerError() throws Exception {
        when(userContextConfig.getUserId()).thenReturn(testUserId);
        when(premiumService.activatePremium(eq(testUserId), any(PremiumDto.class)))
                .thenThrow(new RuntimeException("Service error"));

        mockMvc.perform(post("/premium")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(premiumDto)))
                .andExpect(status().isInternalServerError());
    }
}
