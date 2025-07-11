package com.dprol.social.service;

import com.dprol.social.dto.goal.GoalDto;
import com.dprol.social.dto.goal.GoalFilterDto;
import com.dprol.social.entity.goal.Goal;
import com.dprol.social.entity.goal.GoalStatus;
import com.dprol.social.entity.user.User;
import com.dprol.social.exception.GoalNotFoundException;
import com.dprol.social.exception.UserNotFoundException;
import com.dprol.social.mapper.goal.GoalMapper;
import com.dprol.social.publisher.GoalComplitedPublisher;
import com.dprol.social.repository.UserRepository;
import com.dprol.social.repository.goal.GoalRepository;
import com.dprol.social.service.goal.filter.GoalFilterService;
import com.dprol.social.service.goal.goal.GoalServiceImpl;
import com.dprol.social.validator.goal.GoalValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoalServiceImplTest {

    @Mock
    private GoalComplitedPublisher goalComplitedPublisher;

    @Mock
    private GoalRepository goalRepository;

    @Mock
    private GoalMapper goalMapper;

    @Mock
    private GoalValidator goalValidator;

    @Mock
    private GoalFilterService goalFilterService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GoalServiceImpl goalService;

    // Тестовые данные
    private final Long USER_ID = 1L;
    private final Long GOAL_ID = 10L;
    private GoalDto goalDto;
    private Goal goalEntity;
    private Goal completedGoal;
    private User user;

    // ------------------------ createGoal() ------------------------

    @Test
    void createGoal_Success() {
        // Подготовка
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        doNothing().when(goalValidator).validateGoalByAmountUsers(user);
        when(goalMapper.toEntity(goalDto)).thenReturn(goalEntity);
        when(goalRepository.save(goalEntity)).thenReturn(goalEntity);
        when(goalMapper.toDto(goalEntity)).thenReturn(goalDto);

        // Действие
        GoalDto result = goalService.createGoal(goalDto, USER_ID);

        // Проверка
        assertNotNull(result);
        assertEquals("Learn Java", result.getTitle());
        verify(goalValidator).validateGoalByAmountUsers(user);
        verify(goalRepository).save(goalEntity);
    }

    @Test
    void createGoal_UserNotFound() {
        // Подготовка
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        // Действие + Проверка
        assertThrows(UserNotFoundException.class,
                () -> goalService.createGoal(goalDto, USER_ID));

        verify(goalRepository, never()).save(any());
    }

    @Test
    void createGoal_ValidationFails() {
        // Подготовка
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        doThrow(new ValidationException("Too many goals")).when(goalValidator).validateGoalByAmountUsers(user);

        // Действие + Проверка
        assertThrows(ValidationException.class,
                () -> goalService.createGoal(goalDto, USER_ID));
    }

    // ------------------------ deleteGoal() ------------------------

    @Test
    void deleteGoal_Success() {
        // Действие
        goalService.deleteGoal(GOAL_ID);

        // Проверка
        verify(goalRepository).deleteById(GOAL_ID);
    }

    @Test
    void deleteGoal_RepositoryThrows() {
        // Подготовка
        doThrow(new RuntimeException("DB error")).when(goalRepository).deleteById(GOAL_ID);

        // Действие + Проверка
        assertThrows(RuntimeException.class,
                () -> goalService.deleteGoal(GOAL_ID));
    }

    // ------------------------ getListGoals() ------------------------

    @Test
    void getListGoals_Success() {
        // Подготовка
        GoalFilterDto filterDto = new GoalFilterDto();
        Stream<Goal> goalStream = Stream.of(goalEntity, completedGoal);
        List<GoalDto> dtos = List.of(goalDto, new GoalDto(GOAL_ID, "Learn Java", GoalStatus.completed));

        doNothing().when(goalValidator).validateGoalById(USER_ID);
        when(goalRepository.findGoalsStream(USER_ID)).thenReturn(goalStream);
        when(goalFilterService.filterGoals(goalStream, filterDto)).thenReturn(goalStream);
        when(goalMapper.toDto(any(Goal.class))).thenAnswer(inv -> {
            Goal g = inv.getArgument(0);
            return new GoalDto(g.getId(), g.getTitle(), g.getStatus());
        });

        // Действие
        List<GoalDto> result = goalService.getListGoals(USER_ID, filterDto);

        // Проверка
        assertEquals(2, result.size());
        assertEquals(GoalStatus.completed, result.get(1).getStatus());
        verify(goalValidator).validateGoalById(USER_ID);
        verify(goalFilterService).filterGoals(any(), eq(filterDto));
    }

    @Test
    void getListGoals_ValidationFails() {
        // Подготовка
        doThrow(new ValidationException("Invalid user")).when(goalValidator).validateGoalById(USER_ID);

        // Действие + Проверка
        assertThrows(ValidationException.class,
                () -> goalService.getListGoals(USER_ID, new GoalFilterDto()));
    }

    @Test
    void getListGoals_EmptyResult() {
        // Подготовка
        doNothing().when(goalValidator).validateGoalById(USER_ID);
        when(goalRepository.findGoalsStream(USER_ID)).thenReturn(Stream.empty());
        when(goalFilterService.filterGoals(any(), any())).thenReturn(Stream.empty());

        // Действие
        List<GoalDto> result = goalService.getListGoals(USER_ID, new GoalFilterDto());

        // Проверка
        assertTrue(result.isEmpty());
    }

    // ------------------------ findGoalById() ------------------------

    @Test
    void findGoalById_Success() {
        // Подготовка
        when(goalRepository.findGoalById(GOAL_ID)).thenReturn(Optional.of(goalEntity));

        // Действие
        Goal result = goalService.findGoalById(GOAL_ID);

        // Проверка
        assertNotNull(result);
        assertEquals("Learn Java", result.getTitle());
    }

    @Test
    void findGoalById_NotFound() {
        // Подготовка
        when(goalRepository.findGoalById(GOAL_ID)).thenReturn(Optional.empty());

        // Действие + Проверка
        GoalNotFoundException ex = assertThrows(GoalNotFoundException.class,
                () -> goalService.findGoalById(GOAL_ID));

        assertTrue(ex.getMessage().contains("Goal with id " + GOAL_ID));
    }

    // ------------------------ updateGoal() ------------------------

    @Test
    void updateGoal_SuccessWithEvent() {
        // Подготовка
        GoalDto completedDto = new GoalDto(GOAL_ID, "Learn Java", GoalStatus.completed);

        when(goalRepository.findGoalById(GOAL_ID)).thenReturn(Optional.of(goalEntity));
        doNothing().when(goalValidator).validateGoalNotCompleted(goalEntity);
        when(goalMapper.toEntity(completedDto)).thenReturn(completedGoal);
        when(goalRepository.save(completedGoal)).thenReturn(completedGoal);
        when(goalMapper.toDto(completedGoal)).thenReturn(completedDto);

        // Действие
        GoalDto result = goalService.updateGoal(completedDto, USER_ID, GOAL_ID);

        // Проверка
        assertEquals(GoalStatus.completed, result.getStatus());
        verify(goalComplitedPublisher).publisher(argThat(event ->
                event.getGoalId().equals(GOAL_ID) &&
                        event.getUserId().equals(USER_ID)
        ));
    }

    @Test
    void updateGoal_SuccessWithoutEvent() {
        // Подготовка
        when(goalRepository.findGoalById(GOAL_ID)).thenReturn(Optional.of(goalEntity));
        doNothing().when(goalValidator).validateGoalNotCompleted(goalEntity);
        when(goalMapper.toEntity(goalDto)).thenReturn(goalEntity);
        when(goalRepository.save(goalEntity)).thenReturn(goalEntity);
        when(goalMapper.toDto(goalEntity)).thenReturn(goalDto);

        // Действие
        GoalDto result = goalService.updateGoal(goalDto, USER_ID, GOAL_ID);

        // Проверка
        assertEquals(GoalStatus.active, result.getStatus());
        verify(goalComplitedPublisher, never()).publisher(any());
    }

    @Test
    void updateGoal_GoalNotFound() {
        // Подготовка
        when(goalRepository.findGoalById(GOAL_ID)).thenReturn(Optional.empty());

        // Действие + Проверка
        assertThrows(GoalNotFoundException.class,
                () -> goalService.updateGoal(goalDto, USER_ID, GOAL_ID));
    }

    @Test
    void updateGoal_AlreadyCompleted() {
        // Подготовка
        Goal completedEntity = new Goal(GOAL_ID, "Learn Java", GoalStatus.completed);
        when(goalRepository.findGoalById(GOAL_ID)).thenReturn(Optional.of(completedEntity));
        doThrow(new ValidationException("Goal completed")).when(goalValidator).validateGoalNotCompleted(completedEntity);

        // Действие + Проверка
        assertThrows(ValidationException.class,
                () -> goalService.updateGoal(goalDto, USER_ID, GOAL_ID));
    }

    @Test
    void updateGoal_PublisherThrows_StillSaves() {
        // Подготовка
        GoalDto completedDto = new GoalDto(GOAL_ID, "Learn Java", GoalStatus.completed);

        when(goalRepository.findGoalById(GOAL_ID)).thenReturn(Optional.of(goalEntity));
        doNothing().when(goalValidator).validateGoalNotCompleted(goalEntity);
        when(goalMapper.toEntity(completedDto)).thenReturn(completedGoal);
        when(goalRepository.save(completedGoal)).thenReturn(completedGoal);
        when(goalMapper.toDto(completedGoal)).thenReturn(completedDto);
        doThrow(new RuntimeException("Queue error")).when(goalComplitedPublisher).publisher(any());

        // Действие
        GoalDto result = goalService.updateGoal(completedDto, USER_ID, GOAL_ID);

        // Проверка
        assertNotNull(result);
        verify(goalRepository).save(completedGoal);
    }
}