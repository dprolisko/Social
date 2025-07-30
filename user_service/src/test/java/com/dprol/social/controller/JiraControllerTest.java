package com.dprol.social.controller;

import com.dprol.social.config.UserContextConfig;
import com.dprol.social.controller.jira.JiraController;
import com.dprol.social.dto.JiraDto;
import com.dprol.social.service.jira.JiraService;
import com.github.dockerjava.api.exception.ConflictException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class JiraControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private JiraService jiraService;

    @Mock
    private UserContextConfig userContextConfig;

    private final long testUserId = 1L;

    private JiraDto jiraDto;

    @InjectMocks
    private JiraController jiraController;

    @BeforeEach
    void setUp() {
        jiraDto = JiraDto.builder().jiraId(1L).userId(2L).username("username").password("password").url("url").build();
        mockMvc = MockMvcBuilders.standaloneSetup(jiraController).build();
        objectMapper = new ObjectMapper();
    }

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
                .andExpect(status().isOk());

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
    void addJira_InvalidData_ShouldReturnBadRequest() throws Exception {
        JiraDto invalidDto = new JiraDto();

        mockMvc.perform(post("/account/jira")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isOk());
    }
}
