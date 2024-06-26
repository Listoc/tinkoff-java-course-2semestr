package edu.java.scrapper.service;

import edu.java.scrapper.configuration.KafkaProperties;
import edu.java.scrapper.service.proccesor.UpdatesSender;
import edu.java.shared.model.LinkUpdateRequest;
import org.springframework.kafka.core.KafkaTemplate;

public class ScrapperQueueProducer implements UpdatesSender {
    private final KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate;
    private final KafkaProperties kafkaProperties;

    public ScrapperQueueProducer(
        KafkaTemplate<String, LinkUpdateRequest> kafkaTemplate,
        KafkaProperties kafkaProperties
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaProperties = kafkaProperties;
    }

    public void send(LinkUpdateRequest linkUpdateRequest) {
        kafkaTemplate.send(
            kafkaProperties.updatesTopic().name(),
            linkUpdateRequest
        );
    }
}
