package edu.java.scrapper.model;

public class ApiErrorResponse {
    private final String description;
    private final String code;
    private final String exceptionName;
    private final String exceptionMessage;
    private final StackTraceElement[] stackTrace;

    public ApiErrorResponse(
        String description,
        String code,
        String exceptionName,
        String exceptionMessage,
        StackTraceElement[] stackTrace
    ) {
        this.description = description;
        this.code = code;
        this.exceptionName = exceptionName;
        this.exceptionMessage = exceptionMessage;
        this.stackTrace = stackTrace;
    }
}
