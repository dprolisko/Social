package com.dprol.social.controller;

import com.dprol.social.controller.goal.GoalInvitationController;
import com.dprol.social.dto.goal.GoalInvitationDto;
import com.dprol.social.dto.goal.GoalInvitationFilterDto;
import com.dprol.social.entity.goal.GoalInvitation;
import com.dprol.social.entity.goal.GoalStatus;
import com.dprol.social.service.goal.goalinvitation.GoalInvitationService;
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

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class GoalInvitationControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private GoalInvitationService goalInvitationService;

    private final Long testUserId = 1L;
    private final Long testGoalInvitationId = 3L;
    private final Long testInvitedId = 2L;
    private GoalInvitationDto invitationDto;
    private GoalInvitation invitation;

    @InjectMocks
    private GoalInvitationController goalInvitationController;
    private GoalInvitationFilterDto goalInvitationFilterDto;

    @BeforeEach
    void setUp() {
        invitationDto = GoalInvitationDto.builder().id(1L).invitedId(2L).inviterId(3L).goalId(4L).status(GoalStatus.active).build();
        goalInvitationFilterDto = GoalInvitationFilterDto.builder().inviterId(1L).invitedId(2L).invitedUsername("invitedUsername").inviterUsername("inviterUsername").status(GoalStatus.active).build();
        mockMvc = MockMvcBuilders.standaloneSetup(goalInvitationController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void invite_ShouldReturnCreated() throws Exception {
        when(goalInvitationService.invite(any(GoalInvitationDto.class), eq(testUserId)))
                .thenReturn(invitationDto);

        mockMvc.perform(post("/goalInvitation/invite")
                        .param("userId", testUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invitationDto)))
                .andExpect(status().isOk());
    }

    @Test
    void acceptInvitation_ShouldReturnOk() throws Exception {
        when(goalInvitationService.acceptInvitation(any(GoalInvitationDto.class), eq(testUserId)))
                .thenReturn(invitationDto);

        mockMvc.perform(post("/goalInvitation/accept")
                        .param("userId", testUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invitationDto)))
                .andExpect(status().isOk());
    }

    @Test
    void getInvitationsList_ShouldReturnList() throws Exception {
        List<GoalInvitationDto> invitations = Collections.singletonList(invitationDto);
        when(goalInvitationService.getInvitationsList(eq(testGoalInvitationId), any(GoalInvitationFilterDto.class)))
                .thenReturn(invitations);

        mockMvc.perform(get("/goalInvitation/getInvitationsList")
                        .param("goalInvitationId", testGoalInvitationId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getInvitedList_ShouldReturnList() throws Exception {
        List<GoalInvitationDto> invitations = Collections.singletonList(invitationDto);
        when(goalInvitationService.getInvitedList(eq(testInvitedId), any(GoalInvitationFilterDto.class)))
                .thenReturn(invitations);

        mockMvc.perform(get("/goalInvitation/getInvitedList")
                        .param("invitedId", testInvitedId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(invitationDto.getId()));
    }

    @Test
    void invite_MissingUserId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/goalInvitation/invite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invitationDto)))
                .andExpect(status().isOk());
    }
}
