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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.xml.bind.ValidationException;
import java.time.LocalDateTime;
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
    private GoalDto completedGoalDto;
    private GoalFilterDto goalFilterDto;

    @BeforeEach
    void setUp() {
        goalDto = GoalDto.builder().id(1L).title("title").description("description").deadline(LocalDateTime.now()).status(GoalStatus.active).usersIds(List.of(1L, 2L)).build();
        user = User.builder().id(USER_ID).username("username").build();
        goalEntity = Goal.builder().id(GOAL_ID).title("title").status(GoalStatus.active).build();
        completedGoal = Goal.builder().id(GOAL_ID).title("title").status(GoalStatus.completed).build();
        completedGoalDto = GoalDto.builder().id(GOAL_ID).title("title").status(GoalStatus.completed).build();
        goalFilterDto = GoalFilterDto.builder().goalName("title").goalStatus(GoalStatus.completed).deadline(LocalDateTime.now()).build();
    }

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
        assertEquals("title", result.getTitle());
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
        Stream<Goal> goalStream = Stream.of(goalEntity, completedGoal);
        List<GoalDto> dtos = List.of(goalDto, completedGoalDto);

        doNothing().when(goalValidator).validateGoalById(USER_ID);
        when(goalRepository.findGoalsStream(USER_ID)).thenReturn(goalStream);
        when(goalFilterService.filterGoals(goalStream, goalFilterDto)).thenReturn(goalStream);
        when(goalMapper.toDto(any(Goal.class))).thenAnswer(inv -> {
            Goal g = inv.getArgument(0);
            return new GoalDto(g.getId(), g.getTitle(), g.getStatus(), g.getDeadline(), g.getDescription());
        });

        // Действие
        List<GoalDto> result = goalService.getListGoals(USER_ID, goalFilterDto);

        // Проверка
        assertEquals(2, dtos.size());
        assertEquals(GoalStatus.completed, dtos.get(1).getStatus());
        verify(goalValidator).validateGoalById(USER_ID);
        verify(goalFilterService).filterGoals(any(), eq(goalFilterDto));
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
        assertEquals("title", result.getTitle());
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

        when(goalRepository.findGoalById(GOAL_ID)).thenReturn(Optional.of(goalEntity));
        doNothing().when(goalValidator).validateGoalNotCompleted(goalEntity);
        when(goalMapper.toEntity(completedGoalDto)).thenReturn(completedGoal);
        when(goalRepository.save(completedGoal)).thenReturn(completedGoal);
        when(goalMapper.toDto(completedGoal)).thenReturn(completedGoalDto);

        // Действие
        GoalDto result = goalService.updateGoal(completedGoalDto, USER_ID, GOAL_ID);

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
}