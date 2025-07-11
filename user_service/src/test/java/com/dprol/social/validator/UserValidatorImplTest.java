package com.dprol.social.validator;

import com.dprol.social.entity.user.User;
import com.dprol.social.exception.DataValidationException;
import com.dprol.social.repository.UserRepository;
import com.dprol.social.validator.user.UserValidatorImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserValidatorImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserValidatorImpl validator;

    private User validUser;

    // Тесты для ValidateUser
    @Test
    void validateUser_ShouldReturnUser_WhenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(validUser));

        User result = validator.ValidateUser(1L);

        assertEquals(1L, result.getId());
        assertEquals("user@example.com", result.getEmail());
    }

    @Test
    void validateUser_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> validator.ValidateUser(999L)
        );

        assertEquals("User with id 999 not found", exception.getMessage());
    }

    @Test
    void validateUser_ShouldThrowException_WhenIdIsNull() {
        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> validator.ValidateUser(null)
        );

        assertEquals("User with id null not found", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(longs = {0, -1})
    void validateUser_ShouldHandleInvalidIds(Long id) {
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> validator.ValidateUser(id)
        );

        assertEquals("User with id " + id + " not found", exception.getMessage());
    }

    // Тесты для ValidateUsers
    @Test
    void validateUsers_ShouldReturnList_WhenAllUsersExist() {
        List<Long> ids = List.of(1L, 2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(validUser));

        List<User> result = validator.ValidateUsers(ids);

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }

    @Test
    void validateUsers_ShouldThrowException_WhenOneUserNotFound() {
        List<Long> ids = List.of(1L, 999L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(validUser));
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> validator.ValidateUsers(ids)
        );

        assertEquals("User with id 999 not found", exception.getMessage());
    }

    @Test
    void validateUsers_ShouldReturnEmptyList_WhenInputIsEmpty() {
        List<User> result = validator.ValidateUsers(Collections.emptyList());
        assertTrue(result.isEmpty());
    }

    @Test
    void validateUsers_ShouldThrowException_WhenInputIsNull() {
        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> validator.ValidateUsers(null)
        );

        assertEquals("User IDs list cannot be null", exception.getMessage());
    }

    @Test
    void validateUsers_ShouldHandleDuplicateIds() {
        List<Long> ids = List.of(1L, 1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(validUser));

        List<User> result = validator.ValidateUsers(ids);

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(1L, result.get(1).getId());
    }

    @Test
    void validateUsers_ShouldStopAtFirstNotFound() {
        List<Long> ids = List.of(999L, 1000L);

        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> validator.ValidateUsers(ids)
        );

        assertEquals("User with id 999 not found", exception.getMessage());
        verify(userRepository, never()).findById(1000L);
    }

    // Тест производительности (демонстрационный)
    @Test
    void validateUsers_PerformanceTest() {
        List<Long> ids = List.of(1L, 2L, 3L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(validUser));
        when(userRepository.findById(3L)).thenReturn(Optional.of(validUser));

        long startTime = System.nanoTime();
        List<User> result = validator.ValidateUsers(ids);
        long duration = System.nanoTime() - startTime;

        assertEquals(3, result.size());
        assertTrue(duration < 100_000_000, "Performance check failed");
    }
}
