package com.dprol.social.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ErrorResponse {

    @JsonFormat(pattern = "${yyyy-MM-dd'T'HH:mm:ss}")
    private LocalDateTime timestamp;

    private String url;

    private String message;
}
