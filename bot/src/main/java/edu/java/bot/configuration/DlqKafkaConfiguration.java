package edu.java.bot.configuration;

import edu.java.bot.kafka.DlqData;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class DlqKafkaConfiguration {
    private final KafkaProperties kafkaProperties;

    public DlqKafkaConfiguration(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @Bean
    public NewTopic dlqTopic() {
        return TopicBuilder.name(kafkaProperties.dlqTopic().name())
            .partitions(kafkaProperties.dlqTopic().partitions())
            .replicas(kafkaProperties.dlqTopic().replicas())
            .build();
    }

    @Bean
    public ProducerFactory<String, DlqData> userProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
            "localhost:29092");
        configProps.put(
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
            StringSerializer.class);
        configProps.put(
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
            JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, DlqData> userKafkaTemplate() {
        return new KafkaTemplate<>(userProducerFactory());
    }
}
