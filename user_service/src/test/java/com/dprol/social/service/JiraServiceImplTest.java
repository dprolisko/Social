package com.dprol.social.service;

import com.dprol.social.dto.JiraDto;
import com.dprol.social.entity.Jira;
import com.dprol.social.exception.JiraNotFoundException;
import com.dprol.social.mapper.JiraMapper;
import com.dprol.social.repository.JiraRepository;
import com.dprol.social.service.jira.JiraServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JiraServiceImplTest {

    @Mock
    private JiraRepository jiraRepository;

    @Mock
    private JiraMapper jiraMapper;

    @InjectMocks
    private JiraServiceImpl jiraService;

    // Тестовые данные
    private final Long USER_ID = 1L;
    private final Long JIRA_ID = 10L;
    private JiraDto jiraDto;
    private Jira jiraEntity;

    @BeforeEach
    void setUp() {
        jiraDto = JiraDto.builder().jiraId(JIRA_ID).userId(USER_ID).username("username").password("password").url("url").build();
        jiraEntity = Jira.builder().id(JIRA_ID).username("username").password("password").url("url").build();
    }

    // ------------------------ addJira() ------------------------

    @Test
    void addJira_Success() {
        // Подготовка
        when(jiraMapper.toEntity(jiraDto)).thenReturn(jiraEntity);
        when(jiraRepository.save(jiraEntity)).thenReturn(jiraEntity);
        when(jiraMapper.toDto(jiraEntity)).thenReturn(jiraDto);

        // Действие
        JiraDto result = jiraService.addJira(USER_ID, jiraDto);

        // Проверка
        assertNotNull(result);
        assertEquals(JIRA_ID, result.getJiraId());
        assertEquals(USER_ID, result.getUserId());
        verify(jiraMapper).toEntity(jiraDto);
        verify(jiraRepository).save(jiraEntity);
        verify(jiraMapper).toDto(jiraEntity);
    }

    @Test
    void addJira_MapperThrows() {
        // Подготовка
        when(jiraMapper.toEntity(jiraDto)).thenThrow(new RuntimeException("Mapping error"));

        // Действие + Проверка
        assertThrows(RuntimeException.class,
                () -> jiraService.addJira(USER_ID, jiraDto));

        verify(jiraRepository, never()).save(any());
    }

    @Test
    void addJira_RepositoryThrows() {
        // Подготовка
        when(jiraMapper.toEntity(jiraDto)).thenReturn(jiraEntity);
        when(jiraRepository.save(jiraEntity)).thenThrow(new RuntimeException("DB error"));

        // Действие + Проверка
        assertThrows(RuntimeException.class,
                () -> jiraService.addJira(USER_ID, jiraDto));
    }

    // ------------------------ deleteJira() ------------------------

    @Test
    void deleteJira_Success() {
        // Подготовка
        when(jiraRepository.findByUserId(USER_ID)).thenReturn(Optional.of(jiraEntity));

        // Действие
        jiraService.deleteJira(USER_ID);

        // Проверка
        verify(jiraRepository).delete(jiraEntity);
    }

    @Test
    void deleteJira_NotFound() {
        // Подготовка
        when(jiraRepository.findByUserId(USER_ID)).thenReturn(Optional.empty());

        // Действие + Проверка
        JiraNotFoundException ex = assertThrows(JiraNotFoundException.class,
                () -> jiraService.deleteJira(USER_ID));

        assertTrue(ex.getMessage().contains("Jira account with id " + USER_ID + " not found"));
        verify(jiraRepository, never()).delete(any());
    }

    @Test
    void deleteJira_RepositoryDeleteThrows() {
        // Подготовка
        when(jiraRepository.findByUserId(USER_ID)).thenReturn(Optional.of(jiraEntity));
        doThrow(new RuntimeException("DB error")).when(jiraRepository).delete(jiraEntity);

        // Действие + Проверка
        assertThrows(RuntimeException.class,
                () -> jiraService.deleteJira(USER_ID));
    }

    // ------------------------ getJira() ------------------------

    @Test
    void getJira_Success() {
        // Подготовка
        when(jiraRepository.findByUserId(USER_ID)).thenReturn(Optional.of(jiraEntity));
        when(jiraMapper.toDto(jiraEntity)).thenReturn(jiraDto);

        // Действие
        JiraDto result = jiraService.getJira(USER_ID);

        // Проверка
        assertNotNull(result);
        assertEquals(JIRA_ID, result.getJiraId());
        assertEquals("username", result.getUsername());
        verify(jiraMapper).toDto(jiraEntity);
    }

    @Test
    void getJira_NotFound() {
        // Подготовка
        when(jiraRepository.findByUserId(USER_ID)).thenReturn(Optional.empty());

        // Действие + Проверка
        JiraNotFoundException ex = assertThrows(JiraNotFoundException.class,
                () -> jiraService.getJira(USER_ID));

        assertTrue(ex.getMessage().contains("Jira account with id " + USER_ID + " not found"));
    }

    @Test
    void getJira_MapperThrows() {
        // Подготовка
        when(jiraRepository.findByUserId(USER_ID)).thenReturn(Optional.of(jiraEntity));
        when(jiraMapper.toDto(jiraEntity)).thenThrow(new RuntimeException("Mapping error"));

        // Действие + Проверка
        assertThrows(RuntimeException.class,
                () -> jiraService.getJira(USER_ID));
    }

    // ------------------------ Edge Cases ------------------------

    @Test
    void addJira_UserIdMismatch() {
        // Подготовка
        JiraDto inputDto = new JiraDto(); // Другой USER_ID
        when(jiraMapper.toEntity(inputDto)).thenReturn(jiraEntity);
        when(jiraRepository.save(jiraEntity)).thenReturn(jiraEntity);
        when(jiraMapper.toDto(jiraEntity)).thenReturn(jiraDto);

        // Действие
        JiraDto result = jiraService.addJira(USER_ID, inputDto);

        // Проверка
        assertNotNull(result);
        // Важно: сервис не проверяет соответствие userId в DTO и переданного userId
        // Этот тест показывает поведение системы в текущей реализации
    }
}