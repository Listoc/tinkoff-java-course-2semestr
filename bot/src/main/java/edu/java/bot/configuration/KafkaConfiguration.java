package edu.java.bot.configuration;

import edu.java.bot.kafka.DlqData;
import edu.java.shared.model.LinkUpdateRequest;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.DeserializationException;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConfiguration {
    private final KafkaProperties kafkaProperties;

    public KafkaConfiguration(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @Bean
    public NewTopic updatesTopic() {
        return TopicBuilder.name(kafkaProperties.updatesTopic().name())
            .partitions(kafkaProperties.updatesTopic().partitions())
            .replicas(kafkaProperties.updatesTopic().replicas())
            .build();
    }

    @Bean
    public ConsumerFactory<String, LinkUpdateRequest> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "edu.java.shared.model.LinkUpdateRequest");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "edu.java.shared.model");

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public CommonErrorHandler commonErrorHandler(KafkaTemplate<String, DlqData> kafkaTemplate) {
        BackOff fixedBackOff = new FixedBackOff(0, 0);
        return new DefaultErrorHandler(
            (consumerRecord, e) -> {
                if (e.getCause() instanceof DeserializationException ex) {
                    kafkaTemplate.send(
                        kafkaProperties.dlqTopic().name(),
                        new DlqData(ex.getMessage(), ex.getStackTrace(), new String(ex.getData()))
                    );
                } else {
                    kafkaTemplate.send(
                        kafkaProperties.dlqTopic().name(),
                        new DlqData(
                            e.getCause().getMessage(),
                            e.getCause().getStackTrace(),
                            consumerRecord.value().toString()
                        )
                    );
                }
            },
            fixedBackOff
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, LinkUpdateRequest>
    kafkaListenerContainerFactory(CommonErrorHandler commonErrorHandler) {

        ConcurrentKafkaListenerContainerFactory<String, LinkUpdateRequest> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setCommonErrorHandler(commonErrorHandler);
        return factory;
    }
}
