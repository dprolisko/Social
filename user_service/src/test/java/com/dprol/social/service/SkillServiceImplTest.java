package com.dprol.social.service;

import com.dprol.social.dto.SkillDto;
import com.dprol.social.entity.skill.Skill;
import com.dprol.social.mapper.SkillMapper;
import com.dprol.social.repository.skill.SkillRepository;
import com.dprol.social.service.skill.SkillServiceImpl;
import com.dprol.social.validator.skill.SkillValidator;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SkillServiceImplTest {

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private SkillMapper skillMapper;

    @Mock
    private SkillValidator skillValidator;

    @InjectMocks
    private SkillServiceImpl skillService;

    private Long SKILL_ID = 1L;

    private Long SKILL_ID1 = 2L;
    private Long SKILL_ID2 = 3L;

    private Long USER_ID = 2L;

    // Тестовые данные

    private SkillDto skillDto1;
    private SkillDto skillDto2;
    private Skill skillEntity;
    private Skill skillEntity1;
    private Skill skillEntity2;

    @BeforeEach
    void setUp() {
        skillDto1 = SkillDto.builder().id(SKILL_ID).skillName("java").build();
        skillDto2 = SkillDto.builder().id(SKILL_ID).skillName("python").build();
        skillEntity1 = Skill.builder().id(SKILL_ID1).skillName("java").build();
        skillEntity2 = Skill.builder().id(SKILL_ID2).skillName("python").build();
    }

    // ------------------------ createSkill() ------------------------

    @Test
    void createSkill_Success() {
        // Подготовка
        doNothing().when(skillValidator).validateSkill(skillDto1);
        when(skillMapper.toEntity(skillDto1)).thenReturn(skillEntity);
        when(skillRepository.save(skillEntity)).thenReturn(skillEntity);
        when(skillMapper.toDto(skillEntity)).thenReturn(skillDto1);

        // Действие
        SkillDto result = skillService.createSkill(skillDto1);

        // Проверка
        assertNotNull(result);
        assertEquals("java", result.getSkillName());
        verify(skillValidator).validateSkill(skillDto1);
        verify(skillMapper).toEntity(skillDto1);
        verify(skillRepository).save(skillEntity);
        verify(skillMapper).toDto(skillEntity);
    }

    @Test
    void createSkill_ValidationFails() {
        // Подготовка
        doThrow(new ValidationException("Invalid skill")).when(skillValidator).validateSkill(skillDto1);

        // Действие + Проверка
        assertThrows(ValidationException.class,
                () -> skillService.createSkill(skillDto1));

        verify(skillRepository, never()).save(any());
    }

    // ------------------------ deleteSkill() ------------------------

    @Test
    void deleteSkill_Success() {
        // Подготовка
        doNothing().when(skillValidator).validateSkillById(SKILL_ID);
        doNothing().when(skillRepository).deleteById(SKILL_ID);

        // Действие
        skillService.deleteSkill(SKILL_ID);

        // Проверка
        verify(skillValidator).validateSkillById(SKILL_ID);
        verify(skillRepository).deleteById(SKILL_ID);
    }

    @Test
    void deleteSkill_ValidationFails() {
        // Подготовка
        doThrow(new ValidationException("Skill not found")).when(skillValidator).validateSkillById(SKILL_ID);

        // Действие + Проверка
        assertThrows(ValidationException.class,
                () -> skillService.deleteSkill(SKILL_ID));

        verify(skillRepository, never()).deleteById(any());
    }

    @Test
    void deleteSkill_RepositoryThrows() {
        // Подготовка
        doNothing().when(skillValidator).validateSkillById(SKILL_ID);
        doThrow(new RuntimeException("DB error")).when(skillRepository).deleteById(SKILL_ID);

        // Действие + Проверка
        assertThrows(RuntimeException.class,
                () -> skillService.deleteSkill(SKILL_ID));
    }

    // ------------------------ getAllSkills() ------------------------

    @Test
    void getAllSkills_Success() {
        // Подготовка
        doNothing().when(skillValidator).validateSkillById(USER_ID);
        when(skillRepository.findAllByUserId(USER_ID)).thenReturn(List.of(skillEntity1, skillEntity2));
        when(skillMapper.toDto(any(Skill.class))).thenAnswer(inv -> {
            Skill s = inv.getArgument(0);
            return new SkillDto(s.getId(), s.getSkillName());
        });

        // Действие
        List<SkillDto> result = skillService.getAllSkills(USER_ID);

        // Проверка
        assertEquals(2, result.size());
        assertEquals("java", result.get(0).getSkillName());
        assertEquals("python", result.get(1).getSkillName());
        verify(skillValidator).validateSkillById(USER_ID);
        verify(skillRepository).findAllByUserId(USER_ID);
        verify(skillMapper, times(2)).toDto(any());
    }

    @Test
    void getAllSkills_ValidationFails() {
        // Подготовка
        doThrow(new ValidationException("User not found")).when(skillValidator).validateSkillById(USER_ID);

        // Действие + Проверка
        assertThrows(ValidationException.class,
                () -> skillService.getAllSkills(USER_ID));

        verify(skillRepository, never()).findAllByUserId(any());
    }

    @Test
    void getAllSkills_EmptyResult() {
        // Подготовка
        doNothing().when(skillValidator).validateSkillById(USER_ID);
        when(skillRepository.findAllByUserId(USER_ID)).thenReturn(List.of());

        // Действие
        List<SkillDto> result = skillService.getAllSkills(USER_ID);

        // Проверка
        assertTrue(result.isEmpty());
    }
}
