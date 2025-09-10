package com.corelyon.mvp.app.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
    String message,
    String error,
    int status,
    String path,
    LocalDateTime timestamp,
    List<String> details
) {
    public ErrorResponse(String message, String error, int status, String path) {
        this(message, error, status, path, LocalDateTime.now(), null);
    }
    
    public ErrorResponse(String message, String error, int status, String path, List<String> details) {
        this(message, error, status, path, LocalDateTime.now(), details);
    }
}
