package com.dprol.social.handler;

import com.dprol.social.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j

public class GlobalExceptionHandler {

    @ExceptionHandler(DataValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDataValidationException(DataValidationException e, HttpServletRequest request) {
        log.error("Data Validation error: {}", e.getMessage());
        return buildErrorResponse(e, request);
    }

    @ExceptionHandler(GoalInvitationNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleGoalInvitationNotFoundException(GoalInvitationNotFoundException e, HttpServletRequest request) {
        log.error("Goal invitation validation error {}", e.getMessage());
        return buildErrorResponse(e, request);
    }

    @ExceptionHandler(GoalNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleGoalNotFoundException(GoalNotFoundException e, HttpServletRequest request) {
        log.error("Goal not found: {}", e.getMessage());
        return buildErrorResponse(e, request);
    }

    @ExceptionHandler(JiraNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleJiraNotFoundException(JiraNotFoundException e, HttpServletRequest request) {
        log.error("Jira not found: {}", e.getMessage());
        return buildErrorResponse(e, request);
    }

    @ExceptionHandler(PremiumNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlePremiumNotFoundException(PremiumNotFoundException e, HttpServletRequest request) {
        log.error("Premium not found: {}", e.getMessage());
        return buildErrorResponse(e, request);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUserNotFoundException(UserNotFoundException e, HttpServletRequest request) {
        log.error("User not found: {}", e.getMessage());
        return buildErrorResponse(e, request);
    }

    @ExceptionHandler(EventNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handEventNotFoundException(EventNotFoundException e, HttpServletRequest request) {
        log.error("Event validation error {}", e.getMessage());
        return buildErrorResponse(e, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return e.getBindingResult().getAllErrors().stream()
                .collect(Collectors.toMap(
                        error -> ((FieldError) error).getField(),
                        error -> Objects.requireNonNullElse(error.getDefaultMessage(), "")
                ));
    }

    private ErrorResponse buildErrorResponse(Exception e, HttpServletRequest request) {
        return ErrorResponse.builder().timestamp(LocalDateTime.now()).url(request.getRequestURI()).message(e.getMessage()).build();
    }
}
