package com.dprol.social.service;

import com.dprol.social.config.UserContextConfig;
import com.dprol.social.dto.user.UserDto;
import com.dprol.social.entity.user.User;
import com.dprol.social.exception.UserNotFoundException;
import com.dprol.social.mapper.user.UserMapper;
import com.dprol.social.publisher.ProfileViewPublisher;
import com.dprol.social.service.user.UserService;
import com.dprol.social.service.viewprofile.ViewProfileImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ViewProfileImplTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserContextConfig userContextConfig;

    @Mock
    private ProfileViewPublisher profileViewPublisher;

    @InjectMocks
    private ViewProfileImpl viewProfile;

    // Тестовые данные
    private final Long PROFILE_ID = 1L;
    private final Long VIEWER_ID = 2L;
    private User user;
    private UserDto userDto;

    // ------------------------ show() ------------------------

    @Test
    void show_SameUser_NoEventPublished() {
        // Подготовка
        when(userContextConfig.getUserId()).thenReturn(PROFILE_ID);
        when(userService.findUserById(PROFILE_ID)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        // Действие
        viewProfile.show(PROFILE_ID);

        // Проверка
        verify(userService).findUserById(PROFILE_ID);
        verify(userMapper).toDto(user);
        verify(profileViewPublisher, never()).publisher(any());
    }

    @Test
    void show_DifferentUser_EventPublished() {
        // Подготовка
        when(userContextConfig.getUserId()).thenReturn(VIEWER_ID);
        when(userService.findUserById(PROFILE_ID)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        // Действие
        viewProfile.show(PROFILE_ID);

        // Проверка
        verify(userService).findUserById(PROFILE_ID);
        verify(userMapper).toDto(user);
        verify(profileViewPublisher).publisher(argThat(event ->
                event.getViewerId().equals(PROFILE_ID) &&
                        event.getViewerId().equals(VIEWER_ID)
        ));
    }

    @Test
    void show_UserNotFound() {
        // Подготовка
        when(userContextConfig.getUserId()).thenReturn(VIEWER_ID);
        when(userService.findUserById(PROFILE_ID)).thenThrow(new UserNotFoundException("User not found"));

        // Действие + Проверка
        assertThrows(UserNotFoundException.class,
                () -> viewProfile.show(PROFILE_ID));

        verify(profileViewPublisher, never()).publisher(any());
    }

    @Test
    void show_ContextUserIdNull_EventNotPublished() {
        // Подготовка
        when(userContextConfig.getUserId()).thenReturn(null);
        when(userService.findUserById(PROFILE_ID)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);

        // Действие
        viewProfile.show(PROFILE_ID);

        // Проверка
        verify(userService).findUserById(PROFILE_ID);
        verify(userMapper).toDto(user);
        verify(profileViewPublisher, never()).publisher(any());
    }

    @Test
    void show_EventPublisherThrows_StillCompletes() {
        // Подготовка
        when(userContextConfig.getUserId()).thenReturn(VIEWER_ID);
        when(userService.findUserById(PROFILE_ID)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDto);
        doThrow(new RuntimeException("Queue down")).when(profileViewPublisher).publisher(any());

        // Действие
        viewProfile.show(PROFILE_ID);

        // Проверка
        verify(userService).findUserById(PROFILE_ID);
        verify(userMapper).toDto(user);
        // Исключение не должно прокидываться дальше
    }

    @Test
    void show_MapperThrows_ExceptionPropagated() {
        // Подготовка
        when(userContextConfig.getUserId()).thenReturn(VIEWER_ID);
        when(userService.findUserById(PROFILE_ID)).thenReturn(user);
        when(userMapper.toDto(user)).thenThrow(new RuntimeException("Mapping error"));

        // Действие + Проверка
        assertThrows(RuntimeException.class,
                () -> viewProfile.show(PROFILE_ID));
    }
}
