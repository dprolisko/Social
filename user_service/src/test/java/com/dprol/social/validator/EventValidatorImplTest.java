package com.dprol.social.validator;

import com.dprol.social.entity.event.Event;
import com.dprol.social.exception.DataValidationException;
import com.dprol.social.repository.event.EventRepository;
import com.dprol.social.validator.event.EventValidatorImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EventValidatorImplTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventValidatorImpl eventValidator;

    private final Long testEventId = 1L;
    private final Long testUserId = 100L;
    private Event testEvent;

    @Test
    void validateEventById_ShouldNotThrow_WhenEventExists() {
        when(eventRepository.existsById(testEventId)).thenReturn(true);
        assertDoesNotThrow(() -> eventValidator.validateEventById(testEventId));
    }

    @Test
    void validateEventById_ShouldThrow_WhenEventNotExists() {
        when(eventRepository.existsById(testEventId)).thenReturn(false);

        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> eventValidator.validateEventById(testEventId)
        );

        assertEquals("Event with id 1 does not exist", exception.getMessage());
    }

    @Test
    void validateEventStatus_ShouldNotThrow_WhenEventExists() {
        when(eventRepository.existsById(testEventId)).thenReturn(true);
        assertDoesNotThrow(() -> eventValidator.validateEventStatus(testEvent));
    }

    @Test
    void validateEventStatus_ShouldThrow_WhenEventNotExists() {
        when(eventRepository.existsById(testEventId)).thenReturn(false);

        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> eventValidator.validateEventStatus(testEvent)
        );

        assertEquals("Event with id 1 does not exist", exception.getMessage());
    }

    @Test
    void validateByUserId_ShouldNotThrow_WhenEventExists() {
        when(eventRepository.existsById(testUserId)).thenReturn(true);
        assertDoesNotThrow(() -> eventValidator.validateByUserId(testUserId));
    }

    @Test
    void validateByUserId_ShouldThrow_WhenEventNotExists() {
        when(eventRepository.existsById(testUserId)).thenReturn(false);

        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> eventValidator.validateByUserId(testUserId)
        );

        assertEquals("Event with id 100 does not exist", exception.getMessage());
    }

    // Тест на пограничные значения
    @Test
    void validateEventById_ShouldThrow_ForNullId() {
        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> eventValidator.validateEventById(null)
        );

        assertEquals("Event with id null does not exist", exception.getMessage());
    }

    // Тест на обработку отрицательных ID
    @Test
    void validateEventById_ShouldHandleNegativeId() {
        Long negativeId = -1L;
        when(eventRepository.existsById(negativeId)).thenReturn(false);

        DataValidationException exception = assertThrows(
                DataValidationException.class,
                () -> eventValidator.validateEventById(negativeId)
        );

        assertEquals("Event with id -1 does not exist", exception.getMessage());
    }

    // Тест на обработку null event
    @Test
    void validateEventStatus_ShouldThrow_ForNullEvent() {
        assertThrows(
                NullPointerException.class,
                () -> eventValidator.validateEventStatus(null)
        );
    }
}