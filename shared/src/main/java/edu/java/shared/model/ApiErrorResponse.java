package edu.java.shared.model;

public record ApiErrorResponse(
    String description,
    String code,
    String exceptionName,
    String exceptionMessage,
    StackTraceElement[] stackTrace) {

    public ApiErrorResponse(String description, String code, Exception e) {
        this(description, code, e.getClass().getName(), e.getMessage(), e.getStackTrace());
    }
}
