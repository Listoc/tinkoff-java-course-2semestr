package edu.java.bot.model;

public record ApiErrorResponse(
    String description,
    String code,
    String exceptionName,
    String exceptionMessage,
    StackTraceElement[] stackTrace) {
}
