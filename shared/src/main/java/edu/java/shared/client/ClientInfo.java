package edu.java.shared.client;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

public record ClientInfo(
    @NotNull String url,
    List<Integer> codes,
    BackOffType backOffType,
    Integer maxAttempts,
    Duration duration) {
    private final static List<Integer> DEFAULT_CODES = List.of(500);
    private final static BackOffType DEFAULT_BACKOFF = BackOffType.exponential;
    private final static Integer DEFAULT_ATTEMPTS = 4;
    private final static Duration DEFAULT_DURATION = Duration.ofSeconds(1);

    public ClientInfo(
        String url,
        List<Integer> codes,
        BackOffType backOffType,
        Integer maxAttempts,
        Duration duration
    ) {
        this.url = url;
        this.codes = Objects.requireNonNullElse(codes, DEFAULT_CODES);
        this.backOffType = Objects.requireNonNullElse(backOffType, DEFAULT_BACKOFF);
        this.maxAttempts = Objects.requireNonNullElse(maxAttempts, DEFAULT_ATTEMPTS);
        this.duration = Objects.requireNonNullElse(duration, DEFAULT_DURATION);
    }

    public enum BackOffType {
        exponential,
        linear,
        constant
    }
}
