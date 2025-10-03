package com.dprol.notification_service.handler;



import com.dprol.notification_service.exception.ErrorResponse;
import com.dprol.notification_service.exception.SmsSendingException;
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

    @ExceptionHandler(SmsSendingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDataValidationException(SmsSendingException e, HttpServletRequest request) {
        log.error("Data Validation error: {}", e.getMessage());
        return buildErrorResponse(e, request);
    }

    private ErrorResponse buildErrorResponse(Exception e, HttpServletRequest request) {
        return ErrorResponse.builder().timestamp(LocalDateTime.now()).url(request.getRequestURI()).message(e.getMessage()).build();
    }
}
