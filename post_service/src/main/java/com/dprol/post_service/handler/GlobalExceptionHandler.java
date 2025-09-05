package com.dprol.post_service.handler;

import com.dprol.post_service.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j

public class GlobalExceptionHandler {

    @ExceptionHandler(DataValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDataValidationException(DataValidationException e, HttpServletRequest request) {
        log.error("Data Validation error: {}", e.getMessage());
        return buildErrorResponse(e, request);
    }

    @ExceptionHandler(CommentNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleCommentNotFoundException(CommentNotFoundException e, HttpServletRequest request) {
        log.error("Comment not found: {}", e.getMessage());
        return buildErrorResponse(e, request);
    }

    @ExceptionHandler(PostNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlePostNotFoundException(PostNotFoundException e, HttpServletRequest request) {
        log.error("Post not found: {}", e.getMessage());
        return buildErrorResponse(e, request);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleResourceNotFoundException(ResourceNotFoundException e, HttpServletRequest request) {
        log.error("Resource not found: {}", e.getMessage());
        return buildErrorResponse(e, request);
    }

    private ErrorResponse buildErrorResponse(Exception e, HttpServletRequest request) {
        return ErrorResponse.builder().timestamp(LocalDateTime.now()).url(request.getRequestURI()).message(e.getMessage()).build();
    }
}
