package com.dprol.social.service;

import com.dprol.social.config.UserContextConfig;
import com.dprol.social.dto.user.UserDto;
import com.dprol.social.entity.user.User;
import com.dprol.social.event.ProfileViewEvent;
import com.dprol.social.exception.UserNotFoundException;
import com.dprol.social.mapper.user.UserMapper;
import com.dprol.social.publisher.ProfileViewPublisher;
import com.dprol.social.service.user.UserService;
import com.dprol.social.service.viewprofile.ViewProfileImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.*;
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
    private User profile;
    private User viewer;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        profile = new User();
        profile.setId(1L);
        viewer = new User();
        viewer.setId(2L);
        userDto = new UserDto();
        userDto.setId(1L);
    }

    @Test
    void show_WhenProfileIdDiffersFromViewerId_ShouldPublishEvent_AndMapUser() {
        Long profileId = 20L;
        Long viewerId = 10L;

        when(userService.findUserById(profileId)).thenReturn(profile);
        when(userContextConfig.getUserId()).thenReturn(viewerId);
        when(userMapper.toDto(profile)).thenReturn(null);

        // Чтобы проверить объект события, используем ArgumentCaptor
        ArgumentCaptor<ProfileViewEvent> eventCaptor = ArgumentCaptor.forClass(ProfileViewEvent.class);

        viewProfile.show(profileId);

        verify(userService).findUserById(profileId);
        verify(userContextConfig).getUserId();
        verify(userMapper).toDto(profile);

        verify(profileViewPublisher).publisher(eventCaptor.capture());

        ProfileViewEvent event = eventCaptor.getValue();
        assertEquals(profileId, event.getUserId());
        assertEquals(viewerId, event.getViewerId());
        assertNotNull(event.getTime());
    }

    @Test
    void show_WhenProfileIdEqualsViewerId_ShouldNotPublishEvent_ButMapUser() {
        Long profileId = 10L;
        Long viewerId = 10L;

        when(userService.findUserById(profileId)).thenReturn(profile);
        when(userContextConfig.getUserId()).thenReturn(viewerId);
        when(userMapper.toDto(profile)).thenReturn(null); // Возвращаем null, т.к. результат не используется

        viewProfile.show(profileId);

        verify(userService).findUserById(profileId);
        verify(userContextConfig).getUserId();
        verify(userMapper).toDto(profile);

        verifyNoInteractions(profileViewPublisher);
    }
}