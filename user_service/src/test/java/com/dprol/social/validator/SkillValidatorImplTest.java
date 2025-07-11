package com.dprol.social.validator;

import com.dprol.social.dto.SkillDto;
import com.dprol.social.exception.DataValidationException;
import com.dprol.social.repository.skill.SkillRepository;
import com.dprol.social.validator.skill.SkillValidatorImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SkillValidatorImplTest {

    @Mock
    private SkillRepository skillRepository;

    @InjectMocks
    private SkillValidatorImpl validator;

    private final Long validSkillId = 1L;
    private final Long invalidSkillId = 999L;
    private SkillDto validSkillDto;
    private SkillDto invalidIdDto;
    private SkillDto nullNameDto;
    private SkillDto blankNameDto;
    private SkillDto emptyNameDto;

    // validateSkill tests
    @Test
    void validateSkill_ShouldNotThrow_WhenSkillIsValid() {
        when(skillRepository.existsById(validSkillId)).thenReturn(true);
        assertDoesNotThrow(() -> validator.validateSkill(validSkillDto));
    }

    @Test
    void validateSkill_ShouldThrow_WhenSkillNotExists() {
        when(skillRepository.existsById(invalidSkillId)).thenReturn(false);

        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> validator.validateSkill(invalidIdDto)
        );

        assertEquals("Skill with id 999 not exists", exception.getMessage());
    }

    @Test
    void validateSkill_ShouldThrow_WhenSkillNameIsNull() {
        when(skillRepository.existsById(2L)).thenReturn(true);

        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> validator.validateSkill(nullNameDto)
        );

        assertEquals("Skill name doesn't exist", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    void validateSkill_ShouldThrow_WhenSkillNameIsBlank(String name) {
        SkillDto dto = new SkillDto(3L, name, "Description");
        when(skillRepository.existsById(3L)).thenReturn(true);

        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> validator.validateSkill(dto)
        );

        assertEquals("Skill name is not blank", exception.getMessage());
    }

    @Test
    void validateSkill_ShouldThrow_WhenDtoIsNull() {
        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> validator.validateSkill(null)
        );

        assertEquals("Skill DTO cannot be null", exception.getMessage());
    }

    @Test
    void validateSkill_ShouldThrow_WhenSkillIdIsNull() {
        SkillDto dto = new SkillDto();

        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> validator.validateSkill(dto)
        );

        assertEquals("Skill with id null not exists", exception.getMessage());
    }

    // validateSkillById tests
    @Test
    void validateSkillById_ShouldNotThrow_WhenSkillExists() {
        when(skillRepository.existsById(validSkillId)).thenReturn(true);
        assertDoesNotThrow(() -> validator.validateSkillById(validSkillId));
    }

    @Test
    void validateSkillById_ShouldThrow_WhenSkillNotExists() {
        when(skillRepository.existsById(invalidSkillId)).thenReturn(false);

        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> validator.validateSkillById(invalidSkillId)
        );

        assertEquals("Skill with id 999 not exists", exception.getMessage());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {0, -1})
    void validateSkillById_ShouldHandleInvalidIds(Long id) {
        if (id == null) {
            DataValidationException exception = assertThrows(
                    DataValidationException.class,
                    () -> validator.validateSkillById(null)
            );
            assertEquals("Skill with id null not exists", exception.getMessage());
        } else {
            when(skillRepository.existsById(id)).thenReturn(false);

            DataValidationException exception = assertThrows(
                    DataValidationException.class,
                    () -> validator.validateSkillById(id)
            );
            assertEquals("Skill with id " + id + " not exists", exception.getMessage());
        }
    }

    // Проверка порядка валидации
    @Test
    void validateSkill_ShouldCheckExistenceBeforeName() {
        when(skillRepository.existsById(999L)).thenReturn(false);

        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> validator.validateSkill(invalidIdDto)
        );

        // Проверяем, что ошибка именно о существовании, а не о имени
        assertEquals("Skill with id 999 not exists", exception.getMessage());
        verify(skillRepository, times(1)).existsById(999L);
    }

    // Проверка вызовов репозитория
    @Test
    void validateSkill_ShouldCallRepositoryOnce() {
        when(skillRepository.existsById(1L)).thenReturn(true);
        validator.validateSkill(validSkillDto);
        verify(skillRepository, times(1)).existsById(1L);
    }

    // Тест для максимального значения Long
    @Test
    void validateSkillById_ShouldHandleMaxLongValue() {
        Long maxId = Long.MAX_VALUE;
        when(skillRepository.existsById(maxId)).thenReturn(true);
        assertDoesNotThrow(() -> validator.validateSkillById(maxId));
    }

    // Тест для различных описаний
    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "Valid description", "   "})
    void validateSkill_ShouldIgnoreDescription(String description) {
        SkillDto dto = new SkillDto(5L, "Valid", description);
        when(skillRepository.existsById(5L)).thenReturn(true);
        assertDoesNotThrow(() -> validator.validateSkill(dto));
    }
}