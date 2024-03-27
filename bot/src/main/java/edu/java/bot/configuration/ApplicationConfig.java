package edu.java.bot.configuration;

import edu.java.bot.telegram.MyTelegramBot;
import edu.java.bot.telegram.UserMessageProcessor;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app")
public record ApplicationConfig(
    @NotEmpty
    String telegramToken
) {
    @Bean
    public MyTelegramBot telegramBot(UserMessageProcessor userMessageProcessor) {
        return new MyTelegramBot(telegramToken, userMessageProcessor);
    }
}
