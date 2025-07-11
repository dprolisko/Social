package com.dprol.social.controller;

import com.dprol.social.controller.goal.GoalInvitationController;
import com.dprol.social.dto.goal.GoalInvitationDto;
import com.dprol.social.dto.goal.GoalInvitationFilterDto;
import com.dprol.social.entity.goal.GoalInvitation;
import com.dprol.social.service.goal.goalinvitation.GoalInvitationService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GoalInvitationController.class)
class GoalInvitationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GoalInvitationService goalInvitationService;

    private final Long testUserId = 1L;
    private final Long testGoalInvitationId = 100L;
    private final Long testInvitedId = 2L;
    private GoalInvitationDto invitationDto = new GoalInvitationDto();
    private GoalInvitation invitation = new GoalInvitation();

    @Test
    void invite_ShouldReturnCreated() throws Exception {
        when(goalInvitationService.invite(any(GoalInvitationDto.class), eq(testUserId)))
                .thenReturn(invitationDto);

        mockMvc.perform(post("/goal-invitation/invite")
                        .param("userId", testUserId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invitationDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void acceptInvitation_ShouldReturnOk() throws Exception {
        when(goalInvitationService.acceptInvitation(any(GoalInvitationDto.class), eq(testUserId)))
                .thenReturn(invitationDto);

        mockMvc.perform(post("/goal-invitation/accept")
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

        mockMvc.perform(get("/goal-invitation/invitations")
                        .param("goalInvitationId", testGoalInvitationId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getInvitedList_ShouldReturnList() throws Exception {
        List<GoalInvitationDto> invitations = Collections.singletonList(invitationDto);
        when(goalInvitationService.getInvitedList(eq(testInvitedId), any(GoalInvitationFilterDto.class)))
                .thenReturn(invitations);

        mockMvc.perform(get("/goal-invitation/invited")
                        .param("invitedId", testInvitedId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(invitationDto.getId()));
    }

    @Test
    void findInvitationById_ShouldReturnInvitation() throws Exception {
        when(goalInvitationService.findInvitationById(testGoalInvitationId))
                .thenReturn(invitation);

        mockMvc.perform(get("/goal-invitation/{goalInvitationId}", testGoalInvitationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(invitation.getId()));
    }

    @Test
    void findInvitationById_NotFound_ShouldReturn404() throws Exception {
        when(goalInvitationService.findInvitationById(testGoalInvitationId))
                .thenThrow(new EntityNotFoundException());

        mockMvc.perform(get("/goal-invitation/{goalInvitationId}", testGoalInvitationId))
                .andExpect(status().isNotFound());
    }

    @Test
    void invite_MissingUserId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/goal-invitation/invite")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invitationDto)))
                .andExpect(status().isBadRequest());
    }
}
