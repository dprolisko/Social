package com.dprol.social.service;

import com.dprol.social.dto.goal.GoalInvitationDto;
import com.dprol.social.dto.goal.GoalInvitationFilterDto;
import com.dprol.social.entity.goal.Goal;
import com.dprol.social.entity.goal.GoalInvitation;
import com.dprol.social.entity.goal.GoalStatus;
import com.dprol.social.entity.user.User;
import com.dprol.social.exception.GoalInvitationNotFoundException;
import com.dprol.social.exception.UserNotFoundException;
import com.dprol.social.mapper.goal.GoalInvitationMapper;
import com.dprol.social.repository.UserRepository;
import com.dprol.social.repository.goal.GoalInvitationRepository;
import com.dprol.social.service.goal.filter.GoalInvitationFilterService;
import com.dprol.social.service.goal.goal.GoalService;
import com.dprol.social.service.goal.goalinvitation.GoalInvitationServiceImpl;
import com.dprol.social.service.user.UserService;
import com.dprol.social.validator.goal.goalinvitation.GoalInvitationValidator;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.exceptions.misusing.PotentialStubbingProblem;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoalInvitationServiceImplTest {

    @Mock
    private GoalInvitationRepository goalInvitationRepository;

    @Mock
    private GoalInvitationMapper goalInvitationMapper;

    @Mock
    private GoalInvitationFilterService goalInvitationFilterService;

    @Mock
    private GoalInvitationValidator goalInvitationValidator;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @Mock
    private GoalService goalService;

    @InjectMocks
    private GoalInvitationServiceImpl goalInvitationService;

    // Тестовые данные
    private final Long USER_ID = 1L;
    private final Long INVITED_ID = 4L;
    private final Long GOAL_ID = 2L;
    private final Long INVITATION_ID = 3L;
    private User user;
    private User inviterUser;
    private User invitedUser;
    private Goal goal;
    private GoalInvitationDto invitationDto;
    private GoalInvitation invitationEntity;

    @BeforeEach
    void setUp() {
        invitationDto = GoalInvitationDto.builder().id(1L).goalId(2L).inviterId(3L).invitedId(4L).status(GoalStatus.active).build();
        invitationEntity = GoalInvitation.builder().id(1L).Invited(invitedUser).Inviter(inviterUser).CreatedAt(LocalDateTime.now()).goal(goal).status(GoalStatus.active).build();
        goal = Goal.builder().id(GOAL_ID).status(GoalStatus.active).build();
        user = User.builder().id(USER_ID).username("username").build();
        inviterUser = User.builder().id(INVITATION_ID).username("username").build();
        invitedUser = User.builder().id(INVITED_ID).username("username").build();
    }

    // ------------------------ invite() ------------------------

    @Test
    void invite_Success() {
        // Подготовка
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        doNothing().when(goalInvitationValidator).validateInvitation(USER_ID);
        when(userService.findUserById(INVITED_ID)).thenReturn(invitedUser);
        when(userService.findUserById(INVITATION_ID)).thenReturn(user);
        when(goalService.findGoalById(GOAL_ID)).thenReturn(goal);
        when(goalInvitationMapper.toEntity(user, invitedUser, goal, GoalStatus.active)).thenReturn(invitationEntity);
        when(goalInvitationRepository.save(invitationEntity)).thenReturn(invitationEntity);
        when(goalInvitationMapper.toDto(invitationEntity)).thenReturn(invitationDto);

        // Действие
        GoalInvitationDto result = goalInvitationService.invite(invitationDto, USER_ID);

        // Проверка
        assertNotNull(result);
        assertEquals(USER_ID, result.getId());
        verify(goalInvitationValidator).validateInvitation(USER_ID);
        verify(goalInvitationRepository).save(invitationEntity);
    }

    @Test
    void invite_UserNotFound() {
        // Подготовка
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        // Действие + Проверка
        assertThrows(UserNotFoundException.class,
                () -> goalInvitationService.invite(invitationDto, USER_ID));
    }

    @Test
    void invite_ValidationFails() {
        // Подготовка
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        doThrow(new ValidationException("Invitation limit")).when(goalInvitationValidator).validateInvitation(USER_ID);

        // Действие + Проверка
        assertThrows(ValidationException.class,
                () -> goalInvitationService.invite(invitationDto, USER_ID));
    }

    // ------------------------ acceptInvitation() ------------------------

    @Test
    void acceptInvitation_Success() {
        // Подготовка
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        doNothing().when(goalInvitationValidator).validateInvited(USER_ID);
        when(userService.findUserById(INVITED_ID)).thenReturn(invitedUser);
        when(userService.findUserById(INVITATION_ID)).thenReturn(user);
        when(goalService.findGoalById(GOAL_ID)).thenReturn(goal);
        when(goalInvitationMapper.toEntity(user, invitedUser, goal, GoalStatus.active)).thenReturn(invitationEntity);
        when(goalInvitationRepository.save(invitationEntity)).thenReturn(invitationEntity);
        when(goalInvitationMapper.toDto(invitationEntity)).thenReturn(invitationDto);

        // Действие
        GoalInvitationDto result = goalInvitationService.acceptInvitation(invitationDto, USER_ID);

        // Проверка
        assertNotNull(result);
        assertEquals(GoalStatus.active, result.getStatus());
        verify(goalInvitationValidator).validateInvited(USER_ID);
    }

    @Test
    void acceptInvitation_NotInvitedUser() {
        // Подготовка
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        doThrow(new ValidationException("Not invited")).when(goalInvitationValidator).validateInvited(USER_ID);

        // Действие + Проверка
        assertThrows(ValidationException.class,
                () -> goalInvitationService.acceptInvitation(invitationDto, USER_ID));
    }

    // ------------------------ deleteInvitation() ------------------------

    @Test
    void deleteInvitation_Success() {
        // Действие
        goalInvitationService.deleteInvitation(INVITATION_ID);

        // Проверка
        verify(goalInvitationRepository).deleteById(INVITATION_ID);
    }

    @Test
    void deleteInvitation_RepositoryThrows() {
        // Подготовка
        doThrow(new RuntimeException("DB error")).when(goalInvitationRepository).deleteById(INVITATION_ID);

        // Действие + Проверка
        assertThrows(RuntimeException.class,
                () -> goalInvitationService.deleteInvitation(INVITATION_ID));
    }

    // ------------------------ getInvitationsList() ------------------------

    @Test
    void getInvitationsList_Success() {
        // Подготовка
        GoalInvitationFilterDto filterDto = new GoalInvitationFilterDto();
        List<GoalInvitationDto> expectedDtos = List.of(invitationDto);

        doNothing().when(goalInvitationValidator).validateInvitation(INVITATION_ID);
        when(goalInvitationRepository.findByGoalInvitationStream(INVITATION_ID)).thenReturn(Stream.of(invitationEntity));
        when(goalInvitationFilterService.filterGoalsInvitation(any(), eq(filterDto))).thenReturn(Stream.of(invitationEntity));
        when(goalInvitationMapper.toDto(invitationEntity)).thenReturn(invitationDto);

        // Действие
        List<GoalInvitationDto> result = goalInvitationService.getInvitationsList(INVITATION_ID, filterDto);

        // Проверка
        assertEquals(1, result.size());
        assertEquals(expectedDtos, result);
    }

    @Test
    void getInvitationsList_ValidationFails() {
        // Подготовка
        doThrow(new ValidationException("Invalid invitation")).when(goalInvitationValidator).validateInvitation(INVITATION_ID);

        // Действие + Проверка
        assertThrows(ValidationException.class,
                () -> goalInvitationService.getInvitationsList(INVITATION_ID, new GoalInvitationFilterDto()));
    }

    // ------------------------ getInvitedList() ------------------------

    @Test
    void getInvitedList_Success() {
        // Подготовка
        GoalInvitationFilterDto filterDto = new GoalInvitationFilterDto();

        doNothing().when(goalInvitationValidator).validateInvited(INVITED_ID);
        when(goalInvitationRepository.findByGoalInvitationStream(INVITED_ID)).thenReturn(Stream.of(invitationEntity));
        when(goalInvitationFilterService.filterGoalsInvited(any(), eq(filterDto))).thenReturn(Stream.of(invitationEntity));
        when(goalInvitationMapper.toDto(invitationEntity)).thenReturn(invitationDto);

        // Действие
        List<GoalInvitationDto> result = goalInvitationService.getInvitedList(INVITED_ID, filterDto);

        // Проверка
        assertEquals(1, result.size());
        assertEquals(INVITED_ID, result.get(0).getInvitedId());
        verify(goalInvitationValidator).validateInvited(INVITED_ID);
    }

    @Test
    void getInvitedList_EmptyResult() {
        // Подготовка
        doNothing().when(goalInvitationValidator).validateInvited(INVITED_ID);
        when(goalInvitationRepository.findByGoalInvitationStream(INVITED_ID)).thenReturn(Stream.empty());
        when(goalInvitationFilterService.filterGoalsInvited(any(), any())).thenReturn(Stream.empty());

        // Действие
        List<GoalInvitationDto> result = goalInvitationService.getInvitedList(INVITED_ID, new GoalInvitationFilterDto());

        // Проверка
        assertTrue(result.isEmpty());
    }

    // ------------------------ findInvitationById() ------------------------

    @Test
    void findInvitationById_Success() {
        // Подготовка
        when(goalInvitationRepository.findById(USER_ID)).thenReturn(Optional.of(invitationEntity));

        // Действие
        GoalInvitation result = goalInvitationService.findInvitationById(USER_ID);

        // Проверка
        assertNotNull(result);
        assertEquals(USER_ID, result.getId());
    }

    @Test
    void findInvitationById_NotFound() {
        // Подготовка
        when(goalInvitationRepository.findById(INVITATION_ID)).thenReturn(Optional.empty());

        // Действие + Проверка
        GoalInvitationNotFoundException ex = assertThrows(GoalInvitationNotFoundException.class,
                () -> goalInvitationService.findInvitationById(INVITATION_ID));

        assertEquals("Invitation not found", ex.getMessage());
    }

    // ------------------------ Edge Cases ------------------------

    @Test
    void invite_SameUserInvitation() {
        // Подготовка
        GoalInvitationDto selfInviteDto = new GoalInvitationDto(INVITATION_ID, USER_ID, INVITED_ID, GOAL_ID, GoalStatus.active);

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        doNothing().when(goalInvitationValidator).validateInvitation(USER_ID);
        when(userService.findUserById(USER_ID)).thenReturn(user); // Один и тот же пользователь
        when(goalService.findGoalById(GOAL_ID)).thenReturn(goal);

        // Действие + Проверка
        assertThrows(PotentialStubbingProblem.class,
                () -> goalInvitationService.invite(selfInviteDto, USER_ID));
    }

    @Test
    void acceptInvitation_WrongUser() {
        // Подготовка - USER_ID пытается принять приглашение, предназначенное не ему
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        doThrow(new ValidationException("Not the invited user")).when(goalInvitationValidator).validateInvited(USER_ID);

        // Действие + Проверка
        assertThrows(ValidationException.class,
                () -> goalInvitationService.acceptInvitation(invitationDto, USER_ID));
    }
}
