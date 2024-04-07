package edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.boot.autoconfigure.jooq.DefaultConfigurationCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.annotation.Validated;

@EnableScheduling
@Validated
@ConfigurationProperties(prefix = "app")
public record ApplicationConfig(
    @NotNull
    Scheduler scheduler,
    @NotNull
    AccessType databaseAccessType,
    boolean useQueue
) {
    @Bean
    public Scheduler scheduler() {
        return scheduler;
    }

    @Bean
    public DefaultConfigurationCustomizer postgresJooqCustomizer() {
        return (DefaultConfiguration c) -> c.settings()
            .withRenderSchema(false)
            .withRenderFormatted(true)
            .withRenderQuotedNames(RenderQuotedNames.NEVER);
    }

    public record Scheduler(@NotNull Duration interval) {
    }

    public enum AccessType {
        JDBC, JPA, JOOQ
    }
}
