package edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.updater")
public record UpdaterProperties(
    @NotNull
    Duration interval
) {
    public record Updater(@NotNull Duration duration) {}
}
