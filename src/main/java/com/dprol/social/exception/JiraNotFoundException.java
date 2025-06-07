package com.dprol.social.exception;

public class JiraNotFoundException extends RuntimeException {
    public JiraNotFoundException(String message) {
        super(message);
    }
}
