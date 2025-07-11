package com.dprol.social.controller;

import com.dprol.social.config.UserContextConfig;
import com.dprol.social.controller.jira.JiraController;
import com.dprol.social.dto.JiraDto;
import com.dprol.social.service.jira.JiraService;
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

@WebMvcTest(JiraController.class)
class JiraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JiraService jiraService;

    @MockBean
    private UserContextConfig userContextConfig;

    private final long testUserId = 1L;
    private JiraDto jiraDto;

    @Test
    void addJira_ShouldReturnJiraDto() throws Exception {
        when(userContextConfig.getUserId()).thenReturn(testUserId);
        when(jiraService.addJira(eq(testUserId), any(JiraDto.class))).thenReturn(jiraDto);

        mockMvc.perform(post("/account/jira")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jiraDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(jiraDto.getUsername()))
                .andExpect(jsonPath("$.url").value(jiraDto.getUrl()));
    }

    @Test
    void deleteJira_ShouldReturnNoContent() throws Exception {
        when(userContextConfig.getUserId()).thenReturn(testUserId);
        doNothing().when(jiraService).deleteJira(testUserId);

        mockMvc.perform(delete("/account/jira"))
                .andExpect(status().isNoContent());

        verify(jiraService).deleteJira(testUserId);
    }

    @Test
    void getJira_ShouldReturnJiraDto() throws Exception {
        when(userContextConfig.getUserId()).thenReturn(testUserId);
        when(jiraService.getJira(testUserId)).thenReturn(jiraDto);

        mockMvc.perform(get("/account/jira"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(jiraDto.getUsername()));
    }

    @Test
    void getJira_NotFound_ShouldReturn404() throws Exception {
        when(userContextConfig.getUserId()).thenReturn(testUserId);
        when(jiraService.getJira(testUserId)).thenThrow(new EntityNotFoundException());

        mockMvc.perform(get("/account/jira"))
                .andExpect(status().isNotFound());
    }

    @Test
    void addJira_InvalidData_ShouldReturnBadRequest() throws Exception {
        JiraDto invalidDto = new JiraDto();

        mockMvc.perform(post("/account/jira")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addJira_Conflict_ShouldReturn409() throws Exception {
        when(userContextConfig.getUserId()).thenReturn(testUserId);
        when(jiraService.addJira(eq(testUserId), any(JiraDto.class)))
                .thenThrow(new ConflictException("Jira already exists"));

        mockMvc.perform(post("/account/jira")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jiraDto)))
                .andExpect(status().isConflict());
    }
}
