package edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.url")
public record ClientProperties(
    @NotNull
    String git,
    @NotNull
    String stackOverflow
) {
}
