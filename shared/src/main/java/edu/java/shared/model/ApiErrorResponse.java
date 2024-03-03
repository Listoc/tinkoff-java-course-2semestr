package edu.java.shared.model;

public record ApiErrorResponse(
    String description,
    String code,
    String exceptionName,
    String exceptionMessage,
    StackTraceElement[] stackTrace) {
}
