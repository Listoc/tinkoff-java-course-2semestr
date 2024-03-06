package edu.java.scrapper.configuration;

import edu.java.scrapper.client.GithubClient;
import edu.java.scrapper.client.StackOverflowClient;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@ConfigurationProperties(prefix = "app.url")
public record ClientConfig(
    @NotNull
    String git,
    @NotNull
    String stackOverflow
) {
    @Bean
    public GithubClient githubClient() {
        return new GithubClient(git);
    }

    @Bean
    public StackOverflowClient stackOverflowClient() {
        return new StackOverflowClient(stackOverflow);
    }
}
