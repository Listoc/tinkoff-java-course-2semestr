package edu.java.bot.kafka;

public record DlqData(
    String message,
    StackTraceElement[] stackTraceElements,
    String data
) {
}
