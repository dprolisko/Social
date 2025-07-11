package com.dprol.social.service;

import com.dprol.social.dto.user.UserDto;
import com.dprol.social.entity.user.User;
import com.dprol.social.exception.UserNotFoundException;
import com.dprol.social.mapper.user.UserMapper;
import com.dprol.social.repository.UserRepository;
import com.dprol.social.service.user.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    // Тестовые данные
    private final Long USER_ID = 1L;
    private User userEntity;
    private UserDto userDto;
    private List<Long> USER_IDS;
    private List<User> userEntities;

    // ------------------------ createUser() ------------------------

    @Test
    void createUser_Success() {
        // Подготовка
        when(userMapper.toEntity(userDto)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userMapper.toDto(userEntity)).thenReturn(userDto);

        // Действие
        UserDto result = userService.createUser(userDto);

        // Проверка
        assertNotNull(result);
        assertEquals(USER_ID, result.getId());
        verify(userMapper).toEntity(userDto);
        verify(userRepository).save(userEntity);
        verify(userMapper).toDto(userEntity);
    }

    @Test
    void createUser_NullInput() {
        // Действие + Проверка
        assertThrows(NullPointerException.class,
                () -> userService.createUser(null));
    }

    // ------------------------ updateUser() ------------------------

    @Test
    void updateUser_Success() {
        // Подготовка
        when(userMapper.toEntity(userDto)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userMapper.toDto(userEntity)).thenReturn(userDto);

        // Действие
        UserDto result = userService.updateUser(userDto);

        // Проверка
        assertNotNull(result);
        assertEquals(USER_ID, result.getId());
        verify(userMapper).toEntity(userDto);
        verify(userRepository).save(userEntity);
    }

    @Test
    void updateUser_UserNotFound() {
        // Подготовка
        when(userMapper.toEntity(userDto)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenThrow(new RuntimeException("Optimistic lock error"));

        // Действие + Проверка
        assertThrows(RuntimeException.class,
                () -> userService.updateUser(userDto));
    }

    // ------------------------ deleteUser() ------------------------

    @Test
    void deleteUser_Success() {
        // Подготовка
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(userEntity));
        when(userMapper.toDto(userEntity)).thenReturn(userDto);

        // Действие
        UserDto result = userService.deleteUser(USER_ID);

        // Проверка
        assertNotNull(result);
        assertEquals(USER_ID, result.getId());
        verify(userRepository).delete(userEntity);
        verify(userMapper).toDto(userEntity);
    }

    @Test
    void deleteUser_UserNotFound() {
        // Подготовка
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        // Действие + Проверка
        assertThrows(UserNotFoundException.class,
                () -> userService.deleteUser(USER_ID));
    }

    // ------------------------ getUserById() single ------------------------

    @Test
    void getUserById_Single_Success() {
        // Подготовка
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(userEntity));
        when(userMapper.toDto(userEntity)).thenReturn(userDto);

        // Действие
        UserDto result = userService.getUserById(USER_ID);

        // Проверка
        assertNotNull(result);
        assertEquals(USER_ID, result.getId());
        verify(userMapper).toDto(userEntity);
    }

    @Test
    void getUserById_Single_NotFound() {
        // Подготовка
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        // Действие + Проверка
        assertThrows(UserNotFoundException.class,
                () -> userService.getUserById(USER_ID));
    }

    // ------------------------ getUserById() list ------------------------

    @Test
    void getUserById_List_Success() {
        // Подготовка
        List<UserDto> dtos = userEntities.stream()
                .map(u -> new UserDto(u.getId(), u.getEmail(), u.getUsername(), null))
                .toList();

        when(userRepository.findAllById(USER_IDS)).thenReturn(userEntities);
        when(userMapper.toDto(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            return new UserDto(u.getId(), u.getEmail(), u.getUsername(), null);
        });

        // Действие
        List<UserDto> result = userService.getUserById(USER_IDS);

        // Проверка
        assertEquals(3, result.size());
        assertEquals("Alice", result.get(0).getUsername());
        assertEquals("Bob", result.get(1).getUsername());
        verify(userMapper, times(3)).toDto(any(User.class));
    }

    @Test
    void getUserById_List_EmptyResult() {
        // Подготовка
        when(userRepository.findAllById(USER_IDS)).thenReturn(List.of());

        // Действие
        List<UserDto> result = userService.getUserById(USER_IDS);

        // Проверка
        assertTrue(result.isEmpty());
    }

    // ------------------------ findUserById() ------------------------

    @Test
    void findUserById_Success() {
        // Подготовка
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(userEntity));

        // Действие
        User result = userService.findUserById(USER_ID);

        // Проверка
        assertNotNull(result);
        assertEquals(USER_ID, result.getId());
    }

    @Test
    void findUserById_NotFound() {
        // Подготовка
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        // Действие + Проверка
        UserNotFoundException ex = assertThrows(UserNotFoundException.class,
                () -> userService.findUserById(USER_ID));

        assertTrue(ex.getMessage().contains(USER_ID.toString()));
    }
}