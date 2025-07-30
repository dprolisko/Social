package com.dprol.social.service;

import com.dprol.social.dto.user.UserDto;
import com.dprol.social.entity.user.User;
import com.dprol.social.exception.UserNotFoundException;
import com.dprol.social.mapper.user.UserMapper;
import com.dprol.social.repository.UserRepository;
import com.dprol.social.service.user.UserServiceImpl;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User userEntity;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userEntity = new User();
        userEntity.setId(1L);
        userEntity.setUsername("username");
        userEntity.setPassword("password");
        userEntity.setEmail("email");
        userEntity.setPhone("phone");
        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("username");
        userDto.setPassword("password");
        userDto.setEmail("email");
        userDto.setPhone("phone");
    }

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
        assertEquals(userDto.getId(), result.getId());
        verify(userMapper).toEntity(userDto);
        verify(userRepository).save(userEntity);
        verify(userMapper).toDto(userEntity);
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
        assertEquals(userDto.getId(), result.getId());
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
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(userEntity));
        when(userMapper.toDto(userEntity)).thenReturn(userDto);

        // Действие
        UserDto result = userService.deleteUser(userDto.getId());

        // Проверка
        assertNotNull(result);
        assertEquals(userDto.getId(), result.getId());
        verify(userRepository).delete(userEntity);
        verify(userMapper).toDto(userEntity);
    }

    @Test
    void deleteUser_UserNotFound() {
        // Подготовка
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.empty());

        // Действие + Проверка
        assertThrows(UserNotFoundException.class,
                () -> userService.deleteUser(userDto.getId()));
    }

    // ------------------------ getUserById() single ------------------------

    @Test
    void getUserById_Single_Success() {
        // Подготовка
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(userEntity));
        when(userMapper.toDto(userEntity)).thenReturn(userDto);

        // Действие
        UserDto result = userService.getUserById(userDto.getId());

        // Проверка
        assertNotNull(result);
        assertEquals(userDto.getId(), result.getId());
        verify(userMapper).toDto(userEntity);
    }

    @Test
    void getUserById_Single_NotFound() {
        // Подготовка
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.empty());

        // Действие + Проверка
        assertThrows(UserNotFoundException.class,
                () -> userService.getUserById(userDto.getId()));
    }

    // ------------------------ getUserById() list ------------------------

    @Test
    void getUserById_List_Success() {
        List<Long> ids = List.of(1L, 2L);
        User user2 = new User();
        user2.setId(2L);
        UserDto userDto2 = new UserDto();
        userDto2.setId(2L);
        List<User> users = List.of(userEntity, user2);
        List<UserDto> userDtos = List.of(userDto, userDto2);
        when(userRepository.findAllById(ids)).thenReturn(users);
        when(userMapper.toDto(userEntity)).thenReturn(userDto);
        when(userMapper.toDto(user2)).thenReturn(userDto2);
        List<UserDto> result = userService.getUserById(ids);
        assertEquals(userDtos.size(), result.size());
        assertEquals(userDtos.get(0).getId(), result.get(0).getId());
        assertEquals(userDtos.get(1).getId(), result.get(1).getId());
    }
    // ------------------------ findUserById() ------------------------

    @Test
    void findUserById_Success() {
        // Подготовка
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(userEntity));

        // Действие
        User result = userService.findUserById(userDto.getId());

        // Проверка
        assertNotNull(result);
        assertEquals(userDto.getId(), result.getId());
    }

    @Test
    void findUserById_NotFound() {
        // Подготовка
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.empty());

        // Действие + Проверка
        UserNotFoundException ex = assertThrows(UserNotFoundException.class,
                () -> userService.findUserById(userDto.getId()));

        assertTrue(ex.getMessage().contains(userDto.getId().toString()));
    }
}