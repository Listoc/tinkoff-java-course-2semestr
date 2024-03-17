package edu.java.scrapper.configuration;

import edu.java.scrapper.client.BotClient;
import edu.java.scrapper.client.GithubClient;
import edu.java.scrapper.client.StackOverflowClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {
    private final ClientProperties clientProperties;

    public ClientConfig(ClientProperties clientProperties) {
        this.clientProperties = clientProperties;
    }

    @Bean
    public GithubClient githubClient() {
        return new GithubClient(clientProperties.git());
    }

    @Bean
    public StackOverflowClient stackOverflowClient() {
        return new StackOverflowClient(clientProperties.stackOverflow());
    }

    @Bean
    public BotClient botClient() {
        return new BotClient(clientProperties.bot());
    }
}
