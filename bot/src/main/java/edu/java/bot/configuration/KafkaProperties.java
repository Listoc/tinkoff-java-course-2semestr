package edu.java.bot.configuration;

import edu.java.shared.config.TopicInfo;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.kafka")
public record KafkaProperties(
    TopicInfo updatesTopic,
    TopicInfo dlqTopic
) {
}
