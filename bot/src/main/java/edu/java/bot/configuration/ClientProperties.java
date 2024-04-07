package edu.java.bot.configuration;

import edu.java.shared.client.ClientInfo;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.client")
public record ClientProperties(
    @NotNull
    ClientInfo scrapper
) {
}
