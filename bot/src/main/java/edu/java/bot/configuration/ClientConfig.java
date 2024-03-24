package edu.java.bot.configuration;

import edu.java.bot.client.ScrapperClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfig {
    private final ClientProperties clientProperties;

    public ClientConfig(ClientProperties clientProperties) {
        this.clientProperties = clientProperties;
    }

    @Bean
    public ScrapperClient scrapperClient() {
        return new ScrapperClient(clientProperties.scrapper());
    }
}
