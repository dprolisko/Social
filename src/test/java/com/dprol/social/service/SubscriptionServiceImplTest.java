package com.dprol.social.service;

import com.dprol.social.dto.SubscriptionDto;
import com.dprol.social.dto.user.UserDto;
import com.dprol.social.dto.user.UserFilterDto;
import com.dprol.social.entity.user.User;
import com.dprol.social.event.SubscriptionEvent;
import com.dprol.social.mapper.user.UserMapper;
import com.dprol.social.publisher.SubscriptionPublisher;
import com.dprol.social.repository.SubscriptionRepository;
import com.dprol.social.service.subscription.SubscriptionServiceImpl;
import com.dprol.social.service.user.filter.UserFilterService;
import com.dprol.social.validator.subscription.SubscriptionValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceImplTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private SubscriptionValidator subscriptionValidator;

    @Mock
    private UserFilterService userFilterService;

    @Mock
    private SubscriptionPublisher subscriptionPublisher;

    @InjectMocks
    private SubscriptionServiceImpl subscriptionService;

    private final SubscriptionDto subscriptionDto = new SubscriptionDto(1L, 2L);
    private final UserFilterDto userFilterDto = new UserFilterDto();
    private final UserDto userDto = new UserDto();
    private final User userEntity = new User();

    // Тест для followUser()
    @Test
    void followUser_Success() {
        // Подготовка
        doNothing().when(subscriptionValidator).validateSubscription(subscriptionDto);
        doNothing().when(subscriptionValidator).validateFollowerAndFollowingId(subscriptionDto);

        // Действие
        subscriptionService.followUser(subscriptionDto);

        // Проверка
        verify(subscriptionValidator, times(1)).validateSubscription(subscriptionDto);
        verify(subscriptionValidator, times(1)).validateFollowerAndFollowingId(subscriptionDto);
        verify(subscriptionRepository, times(1)).followUser(1L, 2L);
        verify(subscriptionPublisher, times(1)).publisher(any(SubscriptionEvent.class));
    }

    @Test
    void followUser_ValidationFails_ThrowsException() {
        // Подготовка
        doThrow(new ValidationException("Invalid subscription")).when(subscriptionValidator).validateSubscription(subscriptionDto);

        // Действие + Проверка
        assertThrows(ValidationException.class, () -> subscriptionService.followUser(subscriptionDto));
        verify(subscriptionRepository, never()).followUser(anyLong(), anyLong());
        verify(subscriptionPublisher, never()).publisher(any());
    }

    // Тест для unfollowUser()
    @Test
    void unfollowUser_Success() {
        // Подготовка
        doNothing().when(subscriptionValidator).validateFollowerAndFollowingId(subscriptionDto);

        // Действие
        subscriptionService.unfollowUser(subscriptionDto);

        // Проверка
        verify(subscriptionValidator, times(1)).validateFollowerAndFollowingId(subscriptionDto);
        verify(subscriptionRepository, times(1)).unfollowUser(1L, 2L);
    }

    @Test
    void unfollowUser_ValidationFails_ThrowsException() {
        // Подготовка
        doThrow(new ValidationException("Invalid IDs")).when(subscriptionValidator).validateFollowerAndFollowingId(subscriptionDto);

        // Действие + Проверка
        assertThrows(ValidationException.class, () -> subscriptionService.unfollowUser(subscriptionDto));
        verify(subscriptionRepository, never()).unfollowUser(anyLong(), anyLong());
    }

    // Тест для getFollowers()
    @Test
    void getFollowers_Success() {
        // Подготовка
        Long followeeId = 1L;
        List<User> users = List.of(userEntity);
        when(subscriptionRepository.findByFolloweesId(followeeId)).thenReturn(users.stream());
        when(userFilterService.filterUsers(users.stream(), userFilterDto)).thenReturn(users.stream());
        when(userMapper.toDto(userEntity)).thenReturn(userDto);

        // Действие
        List<UserDto> result = subscriptionService.getFollowers(followeeId, userFilterDto);

        // Проверка
        assertEquals(1, result.size());
        assertEquals(userDto, result.get(0));
        verify(subscriptionRepository, times(1)).findByFolloweesId(followeeId);
        verify(userFilterService, times(1)).filterUsers(any(), eq(userFilterDto));
        verify(userMapper, times(1)).toDto(userEntity);
    }

    @Test
    void getFollowers_EmptyResult() {
        // Подготовка
        Long followeeId = 1L;
        when(subscriptionRepository.findByFolloweesId(followeeId)).thenReturn(Stream.empty());
        when(userFilterService.filterUsers(any(), eq(userFilterDto))).thenReturn(Stream.empty());

        // Действие
        List<UserDto> result = subscriptionService.getFollowers(followeeId, userFilterDto);

        // Проверка
        assertTrue(result.isEmpty());
    }

    // Тест для getFollowings()
    @Test
    void getFollowings_Success() {
        // Подготовка
        Long followerId = 1L;
        List<User> users = List.of(userEntity, userEntity);
        when(subscriptionRepository.findByFollowersId(followerId)).thenReturn(users.stream());
        when(userFilterService.filterUsers(users.stream(), userFilterDto)).thenReturn(users.stream());
        when(userMapper.toDto(userEntity)).thenReturn(userDto);

        // Действие
        List<UserDto> result = subscriptionService.getFollowings(followerId, userFilterDto);

        // Проверка
        assertEquals(2, result.size());
        assertEquals(userDto, result.get(0));
        verify(subscriptionRepository, times(1)).findByFollowersId(followerId);
    }

    // Тест для счетчиков
    @Test
    void getFollowersCount_Success() {
        // Подготовка
        Long followerId = 1L;
        when(subscriptionRepository.findByFollowerId(followerId)).thenReturn(5);

        // Действие
        int count = subscriptionService.getFollowersCount(followerId);

        // Проверка
        assertEquals(5, count);
    }

    @Test
    void getFollowingsCount_Success() {
        // Подготовка
        Long followeeId = 1L;
        when(subscriptionRepository.findByFolloweeId(followeeId)).thenReturn(3);

        // Действие
        int count = subscriptionService.getFollowingsCount(followeeId);

        // Проверка
        assertEquals(3, count);
    }

    // Edge case: публикатор бросает исключение
    @Test
    void followUser_PublisherFails_StillProcesses() {
        // Подготовка
        doNothing().when(subscriptionValidator).validateSubscription(subscriptionDto);
        doNothing().when(subscriptionValidator).validateFollowerAndFollowingId(subscriptionDto);
        doThrow(new RuntimeException("Queue down")).when(subscriptionPublisher).publisher(any());

        // Действие + Проверка (не должно бросать исключение)
        assertDoesNotThrow(() -> subscriptionService.followUser(subscriptionDto));

        // Проверка, что основная логика выполнена
        verify(subscriptionRepository, times(1)).followUser(1L, 2L);
    }

    // Тест на null DTO в фильтрации
    @Test
    void getFollowers_NullFilterDto_Success() {
        // Подготовка
        Long followeeId = 1L;
        when(subscriptionRepository.findByFolloweesId(followeeId)).thenReturn(Stream.of(userEntity));
        when(userFilterService.filterUsers(any(), isNull())).thenReturn(Stream.of(userEntity));
        when(userMapper.toDto(userEntity)).thenReturn(userDto);

        // Действие
        List<UserDto> result = subscriptionService.getFollowers(followeeId, null);

        // Проверка
        assertEquals(1, result.size());
        verify(userFilterService, times(1)).filterUsers(any(), isNull());
    }
}