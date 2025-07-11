package com.dprol.social.controller;

import com.dprol.social.controller.skill.SkillController;
import com.dprol.social.dto.SkillDto;
import com.dprol.social.service.skill.SkillService;
import com.github.dockerjava.api.exception.ConflictException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Arrays;
import java.util.List;

import static java.util.Optional.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SkillController.class)
class SkillControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SkillService skillService;

    private final Long testUserId = 1L;
    private SkillDto skillDto;
    private SkillDto skillDto2;

    @Test
    void createSkill_ShouldReturnCreated() throws Exception {
        when(skillService.createSkill(any(SkillDto.class))).thenReturn(skillDto);

        mockMvc.perform(post("/skill")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(skillDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(skillDto.getId()))
                .andExpect(jsonPath("$.name").value(skillDto.getSkillName()));
    }

    @Test
    void getAllSkills_ShouldReturnList() throws Exception {
        List<SkillDto> skills = Arrays.asList(skillDto, skillDto2);
        when(skillService.getAllSkills(testUserId)).thenReturn(skills);

        mockMvc.perform(get("/skill")
                        .param("userId", testUserId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(skillDto.getId()))
                .andExpect(jsonPath("$[1].id").value(skillDto2.getId()));
    }

    @Test
    void createSkill_InvalidData_ShouldReturnBadRequest() throws Exception {
        SkillDto invalidDto = new SkillDto(); // Некорректные данные

        mockMvc.perform(post("/skill")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllSkills_MissingUserId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/skill"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createSkill_Conflict_ShouldReturnConflict() throws Exception {
        when(skillService.createSkill(any(SkillDto.class)))
                .thenThrow(new ConflictException("Skill already exists"));

        mockMvc.perform(post("/skill")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(skillDto)))
                .andExpect(status().isConflict());
    }

    @Test
    void getAllSkills_NotFound_ShouldReturnEmptyList() throws Exception {
        when(skillService.getAllSkills(testUserId)).thenReturn(List.of());

        mockMvc.perform(get("/skill")
                        .param("userId", testUserId.toString()))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$", empty()));
    }
}
