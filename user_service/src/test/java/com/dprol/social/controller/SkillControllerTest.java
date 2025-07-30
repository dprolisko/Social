package com.dprol.social.controller;

import com.dprol.social.controller.skill.SkillController;
import com.dprol.social.dto.SkillDto;
import com.dprol.social.service.skill.SkillService;
import com.github.dockerjava.api.exception.ConflictException;
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
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static java.util.Optional.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SkillControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private SkillService skillService;

    @InjectMocks
    private SkillController skillController;

    private final Long testUserId = 1L;
    private SkillDto skillDto;
    private SkillDto skillDto2;

    @BeforeEach
    void setUp() {
        skillDto = SkillDto.builder().id(1L).skillName("java").build();
        skillDto2 = SkillDto.builder().id(2L).skillName("python").build();
        mockMvc = MockMvcBuilders.standaloneSetup(skillController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createSkill_ShouldReturnCreated() throws Exception {
        when(skillService.createSkill(any(SkillDto.class))).thenReturn(skillDto);

        mockMvc.perform(post("/skill/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(skillDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(skillDto.getId()))
                .andExpect(jsonPath("$.skillName").value(skillDto.getSkillName()));
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
 }
